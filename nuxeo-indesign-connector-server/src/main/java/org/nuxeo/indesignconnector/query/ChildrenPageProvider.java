/*
 * (C) Copyright 2015 Nuxeo SA (http://nuxeo.com/) and contributors.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser General Public License
 * (LGPL) version 2.1 which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/lgpl-2.1.html
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * Contributors:
 *     Michael GENA
 *
 */
package org.nuxeo.indesignconnector.query;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.DocumentModelList;
import org.nuxeo.ecm.core.api.impl.DocumentModelListImpl;
import org.nuxeo.ecm.platform.query.nxql.CoreQueryDocumentPageProvider;

/**
 * @since 7.10
 */
public class ChildrenPageProvider extends CoreQueryDocumentPageProvider {

    protected static final Log log = LogFactory.getLog(ChildrenPageProvider.class);

    private static final long serialVersionUID = 1L;

    protected int pageNumber;

    @Override
    public List<DocumentModel> getCurrentPage() {

        DocumentModelList docs = (DocumentModelList) super.getCurrentPage();
        String where = "(";
        int count = docs.size();
        if (count > 0) {
            int i = 0;
            for (DocumentModel doc : docs) {
                where += "ecm:ancestorId = '" + doc.getId() + "'";
                i += 1;
                if (i < count) {
                    where += " OR ";
                }
            }
            where += ")";
            String finalNxql = "SELECT * FROM Document WHERE ecm:mixinType = 'Picture' AND " + where
                    + " AND ecm:isVersion = 0 AND  ecm:currentLifeCycleState != 'deleted'";
            return getCoreSession().query(finalNxql);
        } else {
            return new DocumentModelListImpl();
        }

    }
}
