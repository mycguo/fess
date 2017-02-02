/*
 * Copyright 2012-2016 CodeLibs Project and the Others.
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
package org.codelibs.fess.app.service;

import java.util.List;

import javax.annotation.Resource;

import org.codelibs.core.beans.util.BeanUtil;
import org.codelibs.fess.Constants;
import org.codelibs.fess.app.pager.AccessTokenPager;
import org.codelibs.fess.es.config.cbean.AccessTokenCB;
import org.codelibs.fess.es.config.exbhv.AccessTokenBhv;
import org.codelibs.fess.es.config.exentity.AccessToken;
import org.codelibs.fess.mylasta.direction.FessConfig;
import org.dbflute.cbean.result.PagingResultBean;
import org.dbflute.optional.OptionalEntity;

public class AccessTokenService {

    @Resource
    protected AccessTokenBhv accessTokenBhv;

    @Resource
    protected FessConfig fessConfig;

    public List<AccessToken> getAccessTokenList(final AccessTokenPager accessTokenPager) {

        final PagingResultBean<AccessToken> accessTokenList = accessTokenBhv.selectPage(cb -> {
            cb.paging(accessTokenPager.getPageSize(), accessTokenPager.getCurrentPageNumber());
            setupListCondition(cb, accessTokenPager);
        });

        // update pager
        BeanUtil.copyBeanToBean(accessTokenList, accessTokenPager, option -> option.include(Constants.PAGER_CONVERSION_RULE));
        accessTokenPager.setPageNumberList(accessTokenList.pageRange(op -> op.rangeSize(fessConfig.getPagingPageRangeSizeAsInteger()))
                .createPageNumberList());

        return accessTokenList;
    }

    public OptionalEntity<AccessToken> getAccessToken(final String id) {
        return accessTokenBhv.selectByPK(id);
    }

    public void store(final AccessToken accessToken) {

        accessTokenBhv.insertOrUpdate(accessToken, op -> op.setRefresh(true));

    }

    public void delete(final AccessToken accessToken) {

        accessTokenBhv.delete(accessToken, op -> op.setRefresh(true));

    }

    protected void setupListCondition(final AccessTokenCB cb, final AccessTokenPager accessTokenPager) {
        if (accessTokenPager.id != null) {
            cb.query().docMeta().setId_Equal(accessTokenPager.id);
        }
        // TODO Long, Integer, String supported only.

        // setup condition
        cb.query().addOrderBy_Name_Asc();
        cb.query().addOrderBy_CreatedTime_Asc();

        // search

    }

    public OptionalEntity<AccessToken> getAccessTokenByToken(final String token) {
        return accessTokenBhv.selectEntity(cb -> {
            cb.query().setToken_Term(token);
        });
    }

}