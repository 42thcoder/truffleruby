/*
 * Copyright (c) 2015, 2016 Oracle and/or its affiliates. All rights reserved. This
 * code is released under a tri EPL/GPL/LGPL license. You can use it,
 * redistribute it and/or modify it under the terms of the:
 *
 * Eclipse Public License version 1.0
 * GNU General Public License version 2
 * GNU Lesser General Public License version 2.1
 */
package org.truffleruby.stdlib;

import com.oracle.truffle.api.dsl.Specialization;
import org.truffleruby.builtins.CoreClass;
import org.truffleruby.builtins.CoreMethod;
import org.truffleruby.builtins.CoreMethodNode;

@CoreClass("Truffle::Etc")
public abstract class EtcNodes {

    @CoreMethod(names = "nprocessors", needsSelf = false)
    public abstract static class NProcessors extends CoreMethodNode {

        @Specialization
        public int nprocessors() {
            return Runtime.getRuntime().availableProcessors();
        }

    }

}
