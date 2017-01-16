/*
 * Copyright (c) 2016 Oracle and/or its affiliates. All rights reserved. This
 * code is released under a tri EPL/GPL/LGPL license. You can use it,
 * redistribute it and/or modify it under the terms of the:
 *
 * Eclipse Public License version 1.0
 * GNU General Public License version 2
 * GNU Lesser General Public License version 2.1
 */
package org.jruby.truffle.parser;

import org.jcodings.Encoding;
import org.jcodings.specific.ASCIIEncoding;
import org.jruby.truffle.core.rope.Rope;
import org.jruby.truffle.core.rope.RopeOperations;

import java.util.Arrays;

import static org.jruby.truffle.core.rope.CodeRange.CR_UNKNOWN;

public class ParserByteListBuilder {

    private byte[] bytes;
    private int length;
    private Encoding encoding;

    public ParserByteListBuilder() {
        bytes = new byte[16];
        length = 0;
        encoding = ASCIIEncoding.INSTANCE;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public Encoding getEncoding() {
        return encoding;
    }

    public void setEncoding(Encoding encoding) {
        this.encoding = encoding;
    }

    public void append(int b) {
        append((byte) b);
    }

    public void append(byte b) {
        grow(1);
        bytes[length] = b;
        length++;
    }

    public void append(byte[] bytes) {
        append(bytes, 0, bytes.length);
    }

    public void append(Rope other) {
        append(other.getBytes());
    }

    public void append(byte[] appendBytes, int appendStart, int appendLength) {
        grow(appendLength);
        System.arraycopy(appendBytes, appendStart, bytes, length, appendLength);
        length += appendLength;
    }

    public void grow(int extra) {
        if (length + extra > bytes.length) {
            bytes = Arrays.copyOf(bytes, (length + extra) * 2);
        }
    }

    public byte[] getBytes() {
        return Arrays.copyOf(bytes, length);
    }

    public void replace(byte[] bytes) {
        this.bytes = bytes;
    }

    public Rope toRope() {
        return RopeOperations.create(getBytes(), encoding, CR_UNKNOWN);
    }

}
