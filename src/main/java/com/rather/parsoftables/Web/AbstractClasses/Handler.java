package com.rather.parsoftables.Web.AbstractClasses;

import org.jsoup.select.Elements;

public abstract class Handler {
    public abstract String handle(Elements elements, int rows, int columns);
}
