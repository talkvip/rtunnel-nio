/*$Id: $
 --------------------------------------
  Skybility
 ---------------------------------------
  Copyright By Skybility ,All right Reserved
 * author   date   comment
 * jeremy  2012-11-27  Created
*/ 
package com.cloudbility.rtunnel.server; 

import java.util.concurrent.atomic.AtomicBoolean;

import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.MessageEvent;

import com.cloudbility.rtunnel.buffer.Packet;
import com.cloudbility.rtunnel.common.CommonHandler;
import com.skybility.cloudsoft.agent.common.AdvancedProperties;
 
public class CompressPacketHandler extends CommonHandler{
	
	// if package's length less than this,do not compress.
	private int compressThreshold = AdvancedProperties.getInstance().requireInteger("compressThreshold");

	private AtomicBoolean compressed;

	public CompressPacketHandler(AtomicBoolean compressed) {
	    this.compressed = compressed;
    }

	@Override
	public void writeRequested(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
		Object msg = e.getMessage();
		if (msg instanceof Packet && this.compressed.get() && ((Packet)msg).isProtocol(Packet.DATA)) {
			Packet p = (Packet) msg;
			int uncompressed = p.getDataLen();
			if (uncompressed > compressThreshold) {
				p.setCompressed();
			}
		}
		super.writeRequested(ctx, e);
	}

}
