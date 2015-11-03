package nl.utwente.mod05.breakout.input;

import nl.utwente.mod05.breakout.Breakout;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

public class CameraInputHandler extends InputHandler {
	public static final String DEFAULT_INPUT_NAME = "stdin";
	public static final int INPUT_BYTES = 1;
	public static final int INPUT_MAX_WIDTH = 600;
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
		int prev = this.position;
		try {
			//If stream is unavailable, or not enough bytes are available, return the previous
			// position.
			if (this.stream == null || this.stream.available() < INPUT_BYTES) {
				return prev;
			}

			//Read up to INPUT_BYTES into the buffer, otherwise return the previous position.
			byte[] buffer = new byte[INPUT_BYTES];
			if (this.stream.read(buffer) != INPUT_BYTES) {
				return prev;
			}

			//Reconstruct the position, sum up all individual bytes to check if only zeroes were
			// sent. If only zeroes were sent, no position was found.
			//TODO: If no position was found, maybe return ERROR STATE?
			int pos = 0;
			int sum = 0;
			for (int i = 0; i < INPUT_BYTES; i++) {
				pos = (pos << 8) + Byte.toUnsignedInt(buffer[i]);
				sum += Byte.toUnsignedInt(buffer[i]);
			}
			if (sum == 0) {
				return prev;
			}

			//Since pos is an integer, it might be possible it overflowed the max allowed value,
			// cut this off.
			pos &= ((1 << (INPUT_BYTES * 8)) - 1);
			pos = (this.maxWidth * pos) / INPUT_MAX_WIDTH;

			this.position = pos;
			return pos;
		} catch (IOException e) {
			if (Breakout.DEBUG) {
				System.err.println("Can not read from input stream.");
			}
			return InputHandler.ERROR_STATE;
		}
	}

	public synchronized void handle() {
		if (Breakout.DEBUG) {
			System.out.println("Using input: " + this.getClass().getSimpleName());
		}
	}
}
