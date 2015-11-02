package nl.utwente.mod05.breakout.input;

import nl.utwente.mod05.breakout.Breakout;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

public class CameraInputHandler extends InputHandler {
	public static final String DEFAULT_INPUT_NAME = "stdin";
	public static final int INPUT_BITS = 8;
	public static final int INPUT_MAX_WIDTH = 254;
	private ByteBuffer buffer;
	private InputStream stream;
	public CameraInputHandler(int width, String pipeName) {
		super(width);
		try {
			if (pipeName.equals(DEFAULT_INPUT_NAME)) {
				this.stream = System.in;
			} else {
				this.stream = new FileInputStream(pipeName);
			}
		} catch (IOException e) {
			if (Breakout.DEBUG) {
				System.err.println("Can not read from file " + pipeName);
			}
		}
	}


	@Override
	public synchronized int getInput() {
		try {
			if (this.stream.available() > 2) {
				byte[] bytes = new byte[INPUT_BITS / 8 + 1];
				int pos = 0;
				if (this.stream.read(bytes, 0, INPUT_BITS / 2 + 1) == INPUT_BITS / 8 + 1) {
					for (int i = 0; i < INPUT_BITS / 8 + 1; i++) {
						pos = (pos << 4) + bytes[i];
					}
					pos &= (1 << (INPUT_BITS + 1) - 1);
				} else {
					pos = ERROR_STATE;
				}
				if (pos == (1 << (INPUT_BITS + 1) - 1)) {
					pos = ERROR_STATE;
				} else {
					pos = (this.maxWidth * pos) / INPUT_MAX_WIDTH;
				}

				this.position = pos;
			}
		} catch (IOException e) {
			if (Breakout.DEBUG) {
				System.err.println("Can not read from input stream.");
			}
			this.position = InputHandler.ERROR_STATE;
		}
		return this.position;
	}

	public synchronized void handle() {
		if (Breakout.DEBUG) {
			System.out.println("Using input: " + this.getClass().getSimpleName());
		}
	}
}
