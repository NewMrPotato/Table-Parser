package com.rather.parsoftables.Web.AbstractClasses;

import org.jsoup.select.Elements;

public abstract class Parser{
    public abstract Elements parse(String reference, String XPath, int rows, int columns, int indexTable);
}
