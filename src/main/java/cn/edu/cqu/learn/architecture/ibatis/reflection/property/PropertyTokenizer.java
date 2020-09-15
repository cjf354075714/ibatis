package cn.edu.cqu.learn.architecture.ibatis.reflection.property;

import java.util.Iterator;

/**
 * 我不知道这个类有啥用，只能看后面谁在调用
 */
public class PropertyTokenizer implements Iterator<PropertyTokenizer> {

    private String name;
    private final String indexedName;
    private String index;
    private final String children;

    public PropertyTokenizer(String fullName) {
        int charIndex = fullName.indexOf('.');
        if (charIndex > -1) {
            name = fullName.substring(0, charIndex);
            children = fullName.substring(charIndex + 1);
        } else {
            name = fullName;
            children = null;
        }
        indexedName = name;
        charIndex = name.indexOf('[');
        if (charIndex > -1) {
            index = name.substring(charIndex + 1, name.length() - 1);
            name = name.substring(0, charIndex);
        }
    }

    public String getName() {
        return name;
    }

    public String getIndex() {
        return index;
    }

    public String getIndexedName() {
        return indexedName;
    }

    public String getChildren() {
        return children;
    }

    @Override
    public boolean hasNext() {
        return children != null;
    }

    @Override
    public PropertyTokenizer next() {
        return new PropertyTokenizer(children);
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException("Remove is not supported, as it has no meaning in the context of properties.");
    }
}
