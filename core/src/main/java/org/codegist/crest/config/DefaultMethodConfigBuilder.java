package org.codegist.crest.config;

import org.codegist.common.lang.State;
import org.codegist.common.net.Urls;
import org.codegist.crest.CRestProperty;
import org.codegist.crest.entity.EntityWriter;
import org.codegist.crest.entity.UrlEncodedFormEntityWriter;
import org.codegist.crest.entity.multipart.MultiPartEntityWriter;
import org.codegist.crest.handler.ErrorHandler;
import org.codegist.crest.handler.ResponseHandler;
import org.codegist.crest.handler.RetryHandler;
import org.codegist.crest.interceptor.RequestInterceptor;
import org.codegist.crest.serializer.Deserializer;
import org.codegist.crest.serializer.Serializer;
import org.codegist.crest.util.MultiParts;
import org.codegist.crest.util.Registry;

import java.lang.reflect.Method;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.codegist.common.collect.Arrays.*;
import static org.codegist.crest.CRestProperty.*;
import static org.codegist.crest.config.MethodConfig.*;

@SuppressWarnings("unchecked")
class DefaultMethodConfigBuilder extends ConfigBuilder implements MethodConfigBuilder {

    private final Method method;
    private final List<ParamConfigBuilder> extraParamBuilders = new ArrayList<ParamConfigBuilder>();
    private final List<ParamConfigBuilder> methodParamConfigBuilders = new ArrayList<ParamConfigBuilder>();
    private final Registry<String,Deserializer> mimeDeserializerRegistry;
    private final List<String> pathParts = new ArrayList<String>();
    private final List<Deserializer> deserializers = new ArrayList<Deserializer>();

    private String charset;
    private MethodType meth;
    private String contentType;
    private String accept;
    private Integer socketTimeout;
    private Integer connectionTimeout;
    private RequestInterceptor requestInterceptor;
    private ResponseHandler responseHandler;
    private ErrorHandler errorHandler;
    private RetryHandler retryHandler;
    private EntityWriter entityWriter;

    DefaultMethodConfigBuilder(Method method, Map<String, Object> crestProperties) {
        super(crestProperties);
        this.method = method;
        this.mimeDeserializerRegistry = CRestProperty.get(crestProperties, Registry.class.getName() + "#deserializers-per-mime");

        for (int i = 0; i < method.getParameterTypes().length; i++) {
            ParamConfigBuilder pcb = new DefaultParamConfigBuilder(crestProperties, method.getParameterTypes()[i], method.getGenericParameterTypes()[i]);
            this.methodParamConfigBuilders.add(pcb);
        }
    }

    /**
     * @inheritDoc
     */
    public MethodConfig build() throws Exception {
        ParamConfig[] pConfigMethod = build(methodParamConfigBuilders);
        ParamConfig[] extraParams = build(extraParamBuilders);
        String[] paths = arrify(pathParts, String.class);

        // make local copies so that we don't mess with builder state to be able to call build multiple times on it
        String path = paths.length > 1 ? Urls.normalizeSlashes(join("/", paths)) : paths[0];
        Deserializer[] pDeserializers = arrify(this.deserializers, Deserializer.class);

        pDeserializers = defaultIfUndefined(pDeserializers, METHOD_CONFIG_DEFAULT_DESERIALIZERS, DEFAULT_DESERIALIZERS);
        path = defaultIfUndefined(path, METHOD_CONFIG_DEFAULT_PATH, DEFAULT_PATH);
        int pSocketTimeout = defaultIfUndefined(this.socketTimeout, METHOD_CONFIG_DEFAULT_SO_TIMEOUT, DEFAULT_SO_TIMEOUT);
        int pConnectionTimeout = defaultIfUndefined(this.connectionTimeout, METHOD_CONFIG_DEFAULT_CO_TIMEOUT, DEFAULT_CO_TIMEOUT);
        String pCharset = defaultIfUndefined(this.charset, METHOD_CONFIG_DEFAULT_CHARSET, DEFAULT_CHARSET);
        MethodType pMeth = defaultIfUndefined(this.meth, METHOD_CONFIG_DEFAULT_HTTP_METHOD, DEFAULT_METHOD_TYPE);
        String pContentType = defaultIfUndefined(this.contentType, METHOD_CONFIG_DEFAULT_CONTENT_TYPE, DEFAULT_CONTENT_TYPE);
        String pAccept = defaultIfUndefined(this.accept, METHOD_CONFIG_DEFAULT_ACCEPT, DEFAULT_ACCEPT);
        ParamConfig[] pExtraParams = defaultIfUndefined(extraParams, METHOD_CONFIG_DEFAULT_EXTRA_PARAMS, DEFAULT_EXTRA_PARAMS);
        RequestInterceptor pRequestInterceptor = defaultIfUndefined(this.requestInterceptor, METHOD_CONFIG_DEFAULT_REQUEST_INTERCEPTOR, DEFAULT_REQUEST_INTERCEPTOR);
        ResponseHandler pResponseHandler = defaultIfUndefined(this.responseHandler, METHOD_CONFIG_DEFAULT_RESPONSE_HANDLER, DEFAULT_RESPONSE_HANDLER);
        ErrorHandler pErrorHandler = defaultIfUndefined(this.errorHandler, METHOD_CONFIG_DEFAULT_ERROR_HANDLER, DEFAULT_ERROR_HANDLER);
        RetryHandler pRetryHandler = defaultIfUndefined(this.retryHandler, METHOD_CONFIG_DEFAULT_RETRY_HANDLER, DEFAULT_RETRY_HANDLER);
        EntityWriter pEntityWriter = defaultIfUndefined(this.entityWriter, METHOD_CONFIG_DEFAULT_ENTITY_WRITER, DEFAULT_ENTITY_WRITER);

        if(pEntityWriter == null && pMeth.hasEntity()) {
            Class<? extends EntityWriter> entityWriterCls = UrlEncodedFormEntityWriter.class;
            for(ParamConfig cfg : merge(ParamConfig.class, pConfigMethod, pExtraParams)){
                if(MultiParts.hasMultiPart(cfg.getMetaDatas())) {
                    entityWriterCls = MultiPartEntityWriter.class;
                    break;
                }
            }
            pEntityWriter = newInstance(entityWriterCls);
        }

        return new DefaultMethodConfig(
                Charset.forName(pCharset),
                method,
                RegexPathTemplate.create(path),
                pContentType,
                pAccept,
                pMeth,
                pSocketTimeout,
                pConnectionTimeout,
                pEntityWriter,
                pRequestInterceptor,
                pResponseHandler,
                pErrorHandler,
                pRetryHandler,
                pDeserializers,
                pConfigMethod,
                pExtraParams
        );
    }

    private static ParamConfig[] build(List<ParamConfigBuilder> paramConfigBuilders) throws Exception {
        ParamConfig[] pc = new ParamConfig[paramConfigBuilders.size()];
        for (int i = 0; i < paramConfigBuilders.size(); i++) {
            pc[i] = paramConfigBuilders.get(i).build();
        }
        return pc;
    }

    public MethodConfigBuilder setCharset(String charset){
        this.charset = pl(charset);
        return this;
    }

    public MethodConfigBuilder setConsumes(String... mimeTypes) {
        State.notNull(mimeDeserializerRegistry, "Can't lookup a deserializer by mime-type. Please provide a DeserializerFactory");

        String[] mimes = new String[mimeTypes.length];
        for (int i = 0; i < mimeTypes.length; i++) {
            String mMime = pl(mimeTypes[i]);
            this.deserializers.add(mimeDeserializerRegistry.get(mMime));
            mimes[i] = mMime;
        }
        this.accept = join(",", mimes);
        return this;
    }

    public MethodConfigBuilder setProduces(String contentType) {
        this.contentType = pl(contentType);
        return this;
    }

    public ParamConfigBuilder startParamConfig(int index) {
        return methodParamConfigBuilders.get(index);
    }

    public ParamConfigBuilder startExtraParamConfig() {
        ParamConfigBuilder pcb = new DefaultParamConfigBuilder(getCRestProperties());
        extraParamBuilders.add(pcb);
        return pcb;
    }

    public MethodConfigBuilder appendPath(String path) {
        pathParts.add(pl(path));
        return this;
    }

    public MethodConfigBuilder setEndPoint(String endPoint) {
        this.pathParts.add(0, pl(endPoint));
        return this;
    }

    public MethodConfigBuilder setType(MethodType meth) {
        this.meth = meth;
        return this;
    }

    public MethodConfigBuilder setSocketTimeout(int socketTimeout) {
        this.socketTimeout = socketTimeout;
        return this;
    }

    public MethodConfigBuilder setConnectionTimeout(int connectionTimeout) {
        this.connectionTimeout = connectionTimeout;
        return this;
    }

    public MethodConfigBuilder setRequestInterceptor(Class<? extends RequestInterceptor> interceptorCls)  {
        this.requestInterceptor = newInstance(interceptorCls);
        return this;
    }

    public MethodConfigBuilder setResponseHandler(Class<? extends ResponseHandler> responseHandlerClass)   {
        this.responseHandler  = newInstance(responseHandlerClass);
        return this;
    }

    public MethodConfigBuilder setErrorHandler(Class<? extends ErrorHandler> methodHandlerClass)   {
        this.errorHandler = newInstance(methodHandlerClass);
        return this;
    }

    public MethodConfigBuilder setRetryHandler(Class<? extends RetryHandler> retryHandlerClass)   {
        this.retryHandler = newInstance(retryHandlerClass);
        return this;
    }

    public MethodConfigBuilder setEntityWriter(Class<? extends EntityWriter> bodyWriterClass)   {
        this.entityWriter = newInstance(bodyWriterClass);
        return this;
    }

    /* PARAMS SETTINGS METHODS */

    public MethodConfigBuilder setParamsSerializer(Class<? extends Serializer> paramSerializer) {
        for (ParamConfigBuilder b : methodParamConfigBuilders) {
            b.setSerializer(paramSerializer);
        }
        return this;
    }

    public MethodConfigBuilder setParamsEncoded(boolean encoded) {
        for (ParamConfigBuilder b : methodParamConfigBuilders) {
            b.setEncoded(encoded);
        }
        return this;
    }

    public MethodConfigBuilder setParamsListSeparator(String listSeparator) {
        for (ParamConfigBuilder b : methodParamConfigBuilders) {
            b.setListSeparator(listSeparator);
        }
        return this;
    }
}