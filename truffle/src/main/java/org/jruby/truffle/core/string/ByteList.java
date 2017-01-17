/***** BEGIN LICENSE BLOCK *****
 * Version: EPL 1.0/GPL 2.0/LGPL 2.1
 *
 * The contents of this file are subject to the Eclipse Public
 * License Version 1.0 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a copy of
 * the License at http://www.eclipse.org/legal/epl-v10.html
 *
 * Software distributed under the License is distributed on an "AS
 * IS" basis, WITHOUT WARRANTY OF ANY KIND, either express or
 * implied. See the License for the specific language governing
 * rights and limitations under the License.
 *
 * Copyright (C) 2007-2010 JRuby Community
 * Copyright (C) 2007 Charles O Nutter <headius@headius.com>
 * Copyright (C) 2007 Nick Sieger <nicksieger@gmail.com>
 * Copyright (C) 2007 Ola Bini <ola@ologix.com>
 * Copyright (C) 2007 William N Dortch <bill.dortch@gmail.com>
 *
 * Alternatively, the contents of this file may be used under the terms of
 * either of the GNU General Public License Version 2 or later (the "GPL"),
 * or the GNU Lesser General Public License Version 2.1 or later (the "LGPL"),
 * in which case the provisions of the GPL or the LGPL are applicable instead
 * of those above. If you wish to allow use of your version of this file only
 * under the terms of either the GPL or the LGPL, and not to allow others to
 * use your version of this file under the terms of the EPL, indicate your
 * decision by deleting the provisions above and replace them with the notice
 * and other provisions required by the GPL or the LGPL. If you do not delete
 * the provisions above, a recipient may use your version of this file under
 * the terms of any one of the EPL, the GPL or the LGPL.
 ***** END LICENSE BLOCK *****/
package org.jruby.truffle.core.string;

import org.jcodings.Encoding;
import org.jruby.truffle.core.rope.RopeBuilder;

public class ByteList {

    private RopeBuilder ropeBuilder = new RopeBuilder();

    public static ByteList createByteList(int size) {
        final ByteList byteList = new ByteList();
        byteList.ensure(size);
        return byteList;
    }

    public static ByteList createByteList(byte[] bytes, Encoding encoding) {
        final ByteList byteList = new ByteList();
        byteList.append(bytes);
        byteList.setEncoding(encoding);
        return byteList;
    }

    public static ByteList createByteList(byte[] wrap) {
        final ByteList byteList = new ByteList();
        byteList.append(wrap);
        return byteList;
    }

    public static ByteList createByteList(byte[] wrap, int index, int len) {
        final ByteList byteList = new ByteList();
        byteList.append(wrap, index, len);
        return byteList;
    }

    public static ByteList createByteList(byte[] wrap, int index, int len, Encoding encoding) {
        final ByteList byteList = new ByteList();
        byteList.append(wrap, index, len);
        byteList.setEncoding(encoding);
        return byteList;
    }

    public static ByteList createByteList(ByteList wrap, int index, int len) {
        final ByteList byteList = new ByteList();
        byteList.append(wrap, index, len);
        return byteList;
    }

    public void fill(int b, int len) {
        for ( ; --len >= 0; ) {
            append(b);
        }
    }

    public ByteList dup() {
        ByteList dup = new ByteList();
        append(bytes());
        return dup;
    }

    public ByteList dup(int length) {
        ByteList dup = new ByteList();
        ensure(length);
        append(ropeBuilder.getBytes());
        return dup;
    }

    public void ensure(int length) {
        ropeBuilder.getByteArrayBuilder().unsafeEnsureSpace(length);
    }

    public void append(byte b) {
        ropeBuilder.append(b);
    }

    public void append(int b) {
        ropeBuilder.append(b);
    }

    public void append(byte[] moreBytes) {
        append(moreBytes, 0, moreBytes.length);
    }

    public void append(ByteList moreBytes, int index, int len) {
        if (index + len > moreBytes.length()) {
            // TODO S 17-Jan-16 fix this use beyond the known length
            append(moreBytes.getUnsafeBytes(), index, len);
        } else {
            append(moreBytes.bytes(), index, len);
        }
    }

    public void append(byte[] moreBytes, int start, int len) {
        ropeBuilder.append(moreBytes, start, len);
    }

    public int length() {
        return ropeBuilder.getLength();
    }

    public int get(int index) {
        return ropeBuilder.getByteArrayBuilder().getUnsafeBytes()[index];
    }

    public void set(int index, int b) {
        ropeBuilder.getByteArrayBuilder().getUnsafeBytes()[index] = (byte) b;
    }

    public byte[] bytes() {
        return ropeBuilder.getBytes();
    }

    public byte[] getUnsafeBytes() {
        return ropeBuilder.getByteArrayBuilder().getUnsafeBytes();
    }

    public void realSize(int realSize) {
        ropeBuilder.getByteArrayBuilder().setLength(realSize);
    }

    public Encoding getEncoding() {
        return ropeBuilder.getEncoding();
    }

    public void setEncoding(Encoding encoding) {
        ropeBuilder.setEncoding(encoding);
    }

}
