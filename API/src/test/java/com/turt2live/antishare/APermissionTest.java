/*******************************************************************************
 * Copyright (C) 2014 Travis Ralston (turt2live)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/

package com.turt2live.antishare;

import com.turt2live.antishare.engine.list.RejectionList;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.junit.Assert.assertNotNull;

@RunWith(JUnit4.class)
public class APermissionTest {

    @Test
    public void testAllowDeny() {
        for (RejectionList.ListType type : RejectionList.ListType.values()) {
            assertNotNull(APermission.getPermissionNode(true, type));
            assertNotNull(APermission.getPermissionNode(false, type));
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullType1() {
        APermission.getPermissionNode(false, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullType2() {
        APermission.getPermissionNode(true, null);
    }

}
