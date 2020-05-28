package io.github.eutkin.scope;

import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.config.Scope;

import java.util.HashMap;
import java.util.Map;

enum TestMethodScope implements Scope {

    INSTANCE;

    static final String SCOPE_NAME = "test-method";

    static void setExtensionContext() {
        INSTANCE.beans.set(new HashMap<>());
    }

    static void unsetExtensionContext() {
        INSTANCE.beans.remove();
    }

    private final ThreadLocal<Map<String, Object>> beans = ThreadLocal.withInitial(HashMap::new);

    @Override
    public Object get(String name, ObjectFactory<?> objectFactory) {
        return beans.get().computeIfAbsent(name, k -> objectFactory.getObject());
    }

    @Override
    public Object remove(String name) {
        return beans.get().remove(name);
    }

    @Override
    public void registerDestructionCallback(String name, Runnable callback) {

    }

    @Override
    public Object resolveContextualObject(String key) {
        return null;
    }

    @Override
    public String getConversationId() {
        return null;
    }
}
