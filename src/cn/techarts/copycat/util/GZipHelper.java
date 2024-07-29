package cn.techarts.copycat.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import cn.techarts.copycat.Panic;

public class GZipHelper {
	public byte[] zip(byte[] source) {
		if(source == null) return null;
		if(source.length == 0) return source;
		var out = new ByteArrayOutputStream();
		try {
			var gzip = new GZIPOutputStream(out);
			gzip.write(source);
			gzip.close();
			return out.toByteArray();
		}catch(IOException e) {
			throw new Panic(e, "Failed to compress data.");
		}
	}
	
	public byte[] unzip(byte[] source) {
		if(source == null) return null;
		if(source.length == 0) return source;
		byte[] result = null;
        var in = new ByteArrayInputStream(source);
        try {
        	var ungzip = new GZIPInputStream(in);
        	result = ungzip.readAllBytes();
        	ungzip.close();
        } catch (IOException e) {
    		throw new Panic(e, "Failed to decompress data.");
        }
        return result;
 	}
}