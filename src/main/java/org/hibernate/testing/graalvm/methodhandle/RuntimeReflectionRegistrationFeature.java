package org.hibernate.testing.graalvm.methodhandle;

import com.oracle.svm.core.annotate.AutomaticFeature;
import org.graalvm.nativeimage.hosted.Feature;
import org.graalvm.nativeimage.hosted.RuntimeReflection;

// Necessary because for some reason method handles require reflection (?)
@AutomaticFeature
public class RuntimeReflectionRegistrationFeature implements Feature {
    @Override
    public void beforeAnalysis(BeforeAnalysisAccess access) {
        try {
            RuntimeReflection.register(Target1.class);
            RuntimeReflection.register(Target1.class.getDeclaredMethod("foo"));
            RuntimeReflection.register(Target2.class);
            RuntimeReflection.register(Target2.class.getDeclaredMethod("bar"));
        } catch (NoSuchMethodException e) {
            throw new IllegalStateException(e);
        }
    }
}