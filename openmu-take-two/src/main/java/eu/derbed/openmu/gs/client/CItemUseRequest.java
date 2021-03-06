/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.derbed.openmu.gs.client;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.notbed.muonline.util.DataDecrypter;
import com.notbed.muonline.util.Header;

import eu.derbed.openmu.gs.ClientThread;

/**
 * @author Miki
 */
@Header (0x26)
class CItemUseRequest extends SimpleClientPackage {

	private static final Logger log = LoggerFactory.getLogger(CItemUseRequest.class);

	/* (non-Javadoc)
	 * @see eu.derbed.openmu.gs.clientPackage.SimpleClientPackage#process(com.notbed.muonline.util.DataDecrypter, eu.derbed.openmu.gs.ClientThread)
	 */
	@Override
	protected void process(final DataDecrypter dataDecrypter, final ClientThread client) throws IOException {
		final int _slot = dataDecrypter.data[1] & 0xff;
		final int _wid = dataDecrypter.data[2] & 0xff;
		log.debug("Request use item fro window [{}] on slot[{}]", _wid, _slot);
	}

}
