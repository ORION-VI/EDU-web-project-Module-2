package org.example.email;

public interface EmailComposerInterface<B> {

    Email composeByEvent(B object);
}
