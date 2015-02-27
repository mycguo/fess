/*
 * Copyright 2009-2015 the CodeLibs Project and the Others.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */

package jp.sf.fess.util;

import java.util.ArrayList;
import java.util.List;

import jp.sf.fess.Constants;

import org.seasar.framework.beans.util.Copy;

public class FessCopy extends Copy {
    public FessCopy(final Object src, final Object dest) {
        super(src, dest);
    }

    public Copy excludesCommonColumns() {
        return excludesCommonColumns(new CharSequence[0]);
    }

    public Copy excludesCommonColumns(final CharSequence... propertyNames) {
        final List<CharSequence> list = new ArrayList<CharSequence>();
        list.add("searchParams");
        list.add("mode");
        list.add("createdBy");
        list.add("createdTime");
        list.add("updatedBy");
        list.add("updatedTime");
        list.add("deletedBy");
        list.add("deletedTime");
        if (propertyNames.length > 0) {
            for (final CharSequence propertyName : propertyNames) {
                list.add(propertyName);
            }
        }
        return super.excludes(list.toArray(new CharSequence[list.size()]))
                .dateConverter(Constants.DEFAULT_DATETIME_FORMAT,
                        "createdTime", "updatedTime", "deletedTime");
    }

    public Copy commonColumnDateConverter() {
        return dateConverter(Constants.DEFAULT_DATETIME_FORMAT, "createdTime",
                "updatedTime", "deletedTime");
    }

}
