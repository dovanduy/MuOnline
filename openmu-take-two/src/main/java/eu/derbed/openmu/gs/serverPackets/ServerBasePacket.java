package eu.derbed.openmu.gs.serverPackets;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;

import com.notbed.muonline.util.ByteContainer;

/**
 * This class ...
 *
 * @version $Revision: 1.3 $ $Date: 2004/07/04 11:14:53 $
 */
public abstract class ServerBasePacket implements ByteContainer {

	protected ByteArrayOutputStream _bao;

	protected ServerBasePacket() {
		_bao = new ByteArrayOutputStream();
	}

	/**
	 * put the long value to byte array
	 *
	 * @param value
	 */
	protected void writeL(long value) {
		_bao.write((int) (value & 0xff));
		_bao.write((int) (value >> 8 & 0xff));
		_bao.write((int) (value >> 16 & 0xff));
		_bao.write((int) (value >> 24 & 0xff));
	}

	/**
	 * put integer value to bytearray
	 *
	 * @param value
	 */
	protected void writeI(int value) {
		_bao.write(value & 0xff);
		_bao.write(value >> 8 & 0xff);

	}

	/**
	 * writing integer with diverted bits some tims mu protocol using diverted
	 * bits of integer
	 *
	 * @param value
	 */
	protected void writeIDiverted(int value) {
		_bao.write(value >> 8 & 0xff);
		_bao.write(value & 0xff);

	}

	/**
	 * put byte nalue to byte array
	 *
	 * @param value
	 */
	protected void writeC(int value) {
		_bao.write(value & 0xff);
	}

	/**
	 * put double value to bytearray
	 *
	 * @param org
	 */
	protected void writeF(double org) {
		final long value = Double.doubleToRawLongBits(org);
		_bao.write((int) (value & 0xff));
		_bao.write((int) (value >> 8 & 0xff));
		_bao.write((int) (value >> 16 & 0xff));
		_bao.write((int) (value >> 24 & 0xff));
		_bao.write((int) (value >> 32 & 0xff));
		_bao.write((int) (value >> 40 & 0xff));
		_bao.write((int) (value >> 48 & 0xff));
		_bao.write((int) (value >> 56 & 0xff));
	}

	/**
	 * put bytearray to ..
	 *
	 * @param a
	 */
	protected void writeB(byte[] a) {
		try {
			_bao.write(a);
		} catch (final Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * write string to byte array
	 *
	 * @param text
	 */
	protected void writeS(String text) {
		try {
			if (text != null) {
				_bao.write(text.getBytes("ISO-8859-1"));
			}
		} catch (final Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * writes fixed nick full by 0x00 to 10 bytes
	 *
	 * @param nick
	 */
	protected void writeNick(String nick) {
		final int nul = 0;
		try {

			if (nick != null) {
				final int l = nick.length(); // lenght of nnick
				_bao.write(nick.getBytes("ISO-8859-1"));
				for (int i = 0; i < 10 - l; i++) {
					_bao.write(nul);
				}
			}
		} catch (final Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * write strimng to bytearray
	 *
	 * @param text
	 * @param from
	 * @param ile
	 *            size of string to write
	 */
	protected void writeS(String text, int from, int ile) {
		try {
			if (text != null) {
				_bao.write(text.getBytes("ISO-8859-1"), from, ile);
			}
		} catch (final UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * @return
	 */
	protected final byte[] getBytes() {
		return _bao.toByteArray();
	}

	/**
	 * make c1 header template with 1'st class protocol
	 *
	 * @param typ
	 * @param s
	 */
	public void mC1Header(int typ, int s) {
		_bao.write(0xc1);
		_bao.write((byte) s);
		_bao.write((byte) typ);
	}

	/**
	 * make c1 header with 2 clases of protool
	 *
	 * @param typ
	 * @param typ2
	 * @param s
	 */
	public void mC1Header(int typ, int typ2, int s) {
		_bao.write(0xc1);
		_bao.write((byte) s);
		_bao.write((byte) typ);
		_bao.write((byte) typ2);
		// System.out.println("mc1headrec done2");

	}

	/**
	 * make c3 template header with 2 types
	 *
	 * @param typ
	 * @param typ2
	 * @param s
	 */
	public void mC3Header(int typ, int typ2, int s) {
		_bao.write(0xc3);
		_bao.write((byte) s);
		_bao.write((byte) typ);
		_bao.write((byte) typ2);
		// System.out.println("mc1headrec done2");
	}

	public void mC3Header(int typ, int s) {
		_bao.write(0xc3);
		_bao.write((byte) s);
		_bao.write((byte) typ);

		// System.out.println("mc1headrec done2");
	}

	/**
	 * make c2 header with 2 types
	 *
	 * @param typ
	 * @param typ2
	 * @param s
	 */
	public void mC2Header(int typ, int typ2, int s) {
		_bao.write(0xc2);
		_bao.write(s >> 8 & 0xff);
		_bao.write(s & 0xff);
		_bao.write(typ & 0xff);
		_bao.write(typ2 & 0xff);
	}

	/**
	 * Make C2 header with one type
	 *
	 * @param typ
	 * @param s
	 */
	public void mC2Header(int typ, int s) {
		_bao.write(0xc2);
		_bao.write(s >> 8 & 0xff);
		_bao.write(s & 0xff);
		_bao.write(typ & 0xff);

	}

	/**
	 * make c4 header with 2 typs
	 *
	 * @param typ
	 * @param typ2
	 * @param s
	 */
	public void mC4Header(int typ, int typ2, int s) {
		_bao.write(0xc4);
		_bao.write(s >> 8 & 0xff);
		_bao.write(s & 0xff);
		_bao.write(typ & 0xff);
		_bao.write(typ2 & 0xff);
	}

	/**
	 * make c4 header with 1 typ
	 *
	 * @param typ
	 * @param s
	 */
	public void mC4Header(int typ, int s) {
		_bao.write(0xc4);
		_bao.write(s >> 8 & 0xff);
		_bao.write(s & 0xff);
		_bao.write(typ & 0xff);

	}

}
