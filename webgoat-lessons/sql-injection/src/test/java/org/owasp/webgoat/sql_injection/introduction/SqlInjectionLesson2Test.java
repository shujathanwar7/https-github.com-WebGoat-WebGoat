/*
 * This file is part of WebGoat, an Open Web Application Security Project utility. For details, please see http://www.owasp.org/
 *
 * Copyright (c) 2002 - 2019 Bruce Mayhew
 *
 * This program is free software; you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation; either version 2 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program; if
 * not, write to the Free Software Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA
 * 02111-1307, USA.
 *
 * Getting Source ==============
 *
 * Source for this application is maintained at https://github.com/WebGoat/WebGoat, a repository for free software projects.
 */

package org.owasp.webgoat.sql_injection.introduction;

import org.hamcrest.CoreMatchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import javax.sql.DataSource;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles({"webgoat", "test"})
@SpringBootTest
public class SqlInjectionLesson2Test {

    @Value("${webgoat.user.directory}")
    private String webGoatDir;
    private MockMvc mockMvc;
    @Autowired
    private DataSource dataSource;

    @Before
    public void setup() {
        this.mockMvc = standaloneSetup(new SqlInjectionLesson2(dataSource)).build();
    }

    @Test
    public void solution() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/SqlInjection/attack2")
                .param("query", "SELECT department FROM employees WHERE userid=96134;"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.lessonCompleted", CoreMatchers.is(true)));
    }

    @Test
    public void solutionWithQuotes() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/SqlInjection/attack2")
                .param("query", "select * from employees where first_name='Bob' and last_name='Franco'"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.lessonCompleted", CoreMatchers.is(true)));
    }
}