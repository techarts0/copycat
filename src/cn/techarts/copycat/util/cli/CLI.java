/*
 * Copyright (C) 2024 techarts.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package cn.techarts.copycat.util.cli;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import cn.techarts.copycat.Panic;

/**
 * @author rocwon@gmail.com
 */
public class CLI {
	private Object action;
	private Map<String, Method> mapping;
	
	public CLI(Object action) {
		if(action == null) {
			throw Panic.nullAction();
		}
		this.action = action;
		this.mapping = new HashMap<>(32);
		this.parseActions();
	}

	private void parseActions() {
		var clz = action.getClass();
		var methods = clz.getMethods();
		if(methods == null) return;
		if(methods.length == 0) return;
		for(var m : methods) {
			var c = m.getAnnotation(Action.class);
			if(c == null) continue;
			var name = c.name();
			if(name == null) continue;
			this.mapping.put(name, m);
		}
	}
	
	//TODO: Cursor
	public void start(String title, String prompt) {
		if(title != null) {
			System.out.println(title);
		}
		try (var scanner = new Scanner(System.in)) {
			while(true) {
				if(prompt != null) {
					System.out.print(prompt);
				}
				var line = scanner.nextLine();
				if(line == null) continue;
				line = line.trim(); // ' '
				var cmd = getCommand(line);
				var method = mapping.get(cmd);
				if(method == null) continue;
				try {
					method.invoke(action, line);
				} catch (Exception e) {
					System.out.print("Failed to execute the command.");
				}
			}
		}
	}
	
	private String getCommand(String line) {
		var end = line.indexOf(32);
		if(end == -1) return line;
		return line.substring(0, end);
	}
}