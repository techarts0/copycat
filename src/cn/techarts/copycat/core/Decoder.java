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

package cn.techarts.copycat.core;

import cn.techarts.copycat.Panic;

/**
 * The decoder runs on SINGLETON mode.
 * Receives the byte stream from network and convert to protocol frame structure.<br>
 * We provide 4 basic protocol frame style(delimiter, fixed-length, length-field and variable-length-field).<br>
 * Mostly, you can use these decoders directly, don't need to implement a decoder.
 * 
 * @author rocwon@gmail.com
 */
public abstract class Decoder<T extends Frame> implements Cloneable{
	
	protected Class<T> frameClass = null;
	
	public abstract boolean isSingleton();
	
	/**
	 * Recognize the boundary of frame in the byte stream.<p>
	 * Please note that it's not same to the {@link Frame.decode}.
	 */
	public abstract  T[] decode(ByteBuf data);
	
	public Class<T> getFrameClass(){
		return this.frameClass;
	}
	
	public Decoder<T> setFrameClass(Class<T> frameClass) {
		this.frameClass = frameClass;
		return this;
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public Decoder<T> clone(){
		try {
			return (Decoder<T>)super.clone();
		}catch(CloneNotSupportedException e) {
			throw new Panic("Failed to create decoder.");
		}
	}
}