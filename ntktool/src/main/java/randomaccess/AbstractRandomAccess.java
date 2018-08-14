/*
 * Copyright (c) 2007, intarsys consulting GmbH
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * - Redistributions of source code must retain the above copyright notice,
 *   this list of conditions and the following disclaimer.
 *
 * - Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 *
 * - Neither the name of intarsys nor the names of its contributors may be used
 *   to endorse or promote products derived from this software without specific
 *   prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package randomaccess;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Stack;

/**
 * Supports reading and writing to a random access data container. A random
 * access data container behaves like a large array of bytes.
 * 
 */
public abstract class AbstractRandomAccess implements IRandomAccess {
	class MyOutputStream extends OutputStream {
		public MyOutputStream() {
			//
		}

		/**
		 * must move with own pointer through random access!!
		 */
		private long offset = 0;

		public void write(int b) throws IOException {
			AbstractRandomAccess.this.seek(offset);
			AbstractRandomAccess.this.write(b);
			offset++;
		}

		public void write(byte[] b) throws IOException {
			AbstractRandomAccess.this.seek(offset);
			AbstractRandomAccess.this.write(b);
			offset += b.length;
		}

		public void write(byte[] b, int off, int len) throws IOException {
			AbstractRandomAccess.this.seek(offset);
			AbstractRandomAccess.this.write(b, off, len);
			offset += len;
		}

		public void close() throws IOException {
			// do not close
		}
	}

	class MyInputStream extends InputStream {
		public MyInputStream() {
			//
		}

		/**
		 * must move with own pointer through random access!!
		 */
		private long offset = 0;

		public int read() throws IOException {
			AbstractRandomAccess.this.seek(offset);
			int i = AbstractRandomAccess.this.read();
			if (i != -1) {
				offset++;
			}
			return i;
		}

		public int read(byte[] b, int off, int len) throws IOException {
			AbstractRandomAccess.this.seek(offset);
			int i = AbstractRandomAccess.this.read(b, off, len);
			if (i != -1) {
				offset += i;
			}
			return i;
		}

		public int read(byte[] b) throws IOException {
			AbstractRandomAccess.this.seek(offset);
			int i = AbstractRandomAccess.this.read(b);
			if (i != -1) {
				offset += i;
			}
			return i;
		}

		public void close() throws IOException {
			// do not close....
		}
	}

	private Stack<Long> positionStack;

	public AbstractRandomAccess() {
		positionStack = new Stack<Long>();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.intarsys.tools.randomaccess.IRandomAccessData#getInputStream()
	 */
	public InputStream asInputStream() {
		return new MyInputStream();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.intarsys.tools.randomaccess.IRandomAccessData#getOutputStream()
	 */
	public OutputStream asOutputStream() {
		return new MyOutputStream();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.intarsys.tools.randomaccess.IRandomAccessData#mark()
	 */
	public void mark() throws IOException {
		getPositionStack().push(new Long(getOffset()));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.intarsys.tools.randomaccess.IRandomAccessData#reset()
	 */
	public void reset() throws IOException {
		if (getPositionStack().isEmpty()) {
			seek(0);
		} else {
			seek(((Long) getPositionStack().pop()).longValue());
		}
	}

	/**
	 * @return Returns the positionStack.
	 */
	protected Stack<Long> getPositionStack() {
		return positionStack;
	}
}