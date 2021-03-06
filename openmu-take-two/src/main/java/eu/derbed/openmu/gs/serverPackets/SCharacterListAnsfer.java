package eu.derbed.openmu.gs.serverPackets;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.derbed.openmu.gs.muObjects.MuCharacterBase;
import eu.derbed.openmu.gs.muObjects.MuCharacterList;

/**
 *
 */

/**
 * @author MikiOne
 *
 */
public class SCharacterListAnsfer extends ServerBasePacket {

	private static final Logger log = LoggerFactory.getLogger(SCharacterListAnsfer.class);

	/**
	 *
	 */
	MuCharacterList _list;

	public SCharacterListAnsfer(MuCharacterList list) {
		_list = list;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see ServerBasePacket#getContent()
	 */

	// private byte[] mSubCharacter(int s) {
	// byte t[];
	// return t;
	// }

	@Override
	public byte[] getContent() throws IOException {
		final int ilec = _list.getCharsCount();
		log.debug("Number of characters: {}", ilec);
		final CharHea head = new CharHea(ilec);
		_bao.write(head.getContent());
		if (ilec != 0) {
			for (int s = 0; s < ilec; s++) {
				final CharSub t = new CharSub(s, _list.getChar(s));
				_bao.write(t.getContent());
			}
		}

		return _bao.toByteArray();
	}

	class CharSub extends ServerBasePacket {

		private final MuCharacterBase _c;

		private final int _nr;

		public CharSub(int nr, MuCharacterBase base) {
			_c = base;
			_nr = nr;
		}

		@Override
		public byte[] getContent() throws IOException {
			if (_c == null) {
				System.out.println("error");
			}
			_bao.write((byte) _nr);
			writeNick(_c.getName());
			_bao.write(0x00);
			writeI(_c.getLvl());
			_bao.write(0x00); // ctlcode
			_bao.write((byte) _c.getClas());
			writeB(_c.getWear().getBytes());
			return _bao.toByteArray();
		}

	}

	class CharHea extends ServerBasePacket {
		private final int ile_c;

		public CharHea(int ile) {
			ile_c = ile;

		}

		@Override
		public byte[] getContent() throws IOException {
			final byte t[] = { (byte) 0xc1, (byte) 0xff, (byte) 0xf3,
					(byte) 0x00, (byte) 0x04, (byte) 0x00, (byte) 0xff };
			if (ile_c == 0) {
				t[1] = 7;
				t[6] = 0x00;
				final byte[] t1 = new byte[8];
				System.arraycopy(t, 0, t1, 0, 6);
				t1[7] = 0x00;

				return t1;
			}
			t[6] = (byte) ile_c; // how many haracters we have
			t[1] = (byte) (7 + ((ile_c) * 28)); // Size of new package
			return t;
		}

	}

}
