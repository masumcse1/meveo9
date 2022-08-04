

/*
 * (C) Copyright 2018-2020 Webdrone SAS (https://www.webdrone.fr/) and contributors.
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the
 * GNU Affero General Public License as published by the Free Software Foundation, either version 3
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. This program is
 * not suitable for any direct or indirect application in MILITARY industry See the GNU Affero
 * General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License along with this program.
 * If not, see <http://www.gnu.org/licenses/>.
 */

package com.blackbear.flatworm;

import java.io.IOException;
import java.io.InputStream;
import java.net.URLConnection;
import java.io.Reader;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import com.blackbear.flatworm.errors.FlatwormParserException;
import java.net.URL;

public class FTPFileParser extends FileParser
{
    private final URL url;
    
    public FTPFileParser(final String config, final URL url) throws FlatwormParserException {
        super(config, null);
        this.url = url;
    }
    
    @Override
    public void open() throws IOException {
        final URLConnection con = this.url.openConnection();
        final InputStream in = con.getInputStream();
        final String encoding = this.ff.getEncoding();
        this.bufIn = new BufferedReader(new InputStreamReader(in, encoding));
        in.close();
    }
}
