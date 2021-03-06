/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.derbed.openmu.gs.serverPackets;

import java.io.IOException;

/**
 * 
 * @author Miki i Linka
 */
public class SPublicMsg extends ServerBasePacket {
	private final String _who;
	private final String _what;

	public SPublicMsg(String _who, String _what) {
		this._who = _who;
		this._what = _what;
	}

	@Override
	public byte[] getContent() throws IOException {
		mC1Header(0x00, _what.length() + 14);
		writeNick(_who);
		writeS(_what);
		writeC(0x00);
		return getBytes();
	}

}
