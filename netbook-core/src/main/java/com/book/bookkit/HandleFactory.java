package com.book.bookkit;

public interface HandleFactory{

    <T extends WebPageHandle> T createPageHandle(Class<T> c);

    <T extends BookHandler> T createPageHandle(String beanName);
}
