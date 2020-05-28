package io.github.eutkin.scope;

import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

class CustomSpringExtension implements BeforeEachCallback, AfterEachCallback {

    @Override
    public void beforeEach(ExtensionContext context) {
        TestMethodScope.setExtensionContext();
    }

    @Override
    public void afterEach(ExtensionContext context) {
        TestMethodScope.unsetExtensionContext();
    }
}
