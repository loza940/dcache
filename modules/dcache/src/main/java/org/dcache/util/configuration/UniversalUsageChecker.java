/* dCache - http://www.dcache.org/
 *
 * Copyright (C) 2013 - 2018 Deutsches Elektronen-Synchrotron
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.dcache.util.configuration;

import java.util.Properties;

/**
 * A simple implementation of UsageChecker that declares all properties as
 * standard.  This has the effect of silencing any warnings from non-standard
 * properties.
 */
public class UniversalUsageChecker implements UsageChecker
{
    @Override
    public boolean isStandardProperty(Properties defaults, String name)
    {
        return true;
    }
}
