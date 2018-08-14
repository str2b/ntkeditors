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
import java.io.RandomAccessFile;

/**
 * An interface for an object that can randomly access bytes in a data stream.
 * <p>
 * This is an abstraction of {@link RandomAccessFile} to support other data
 * storage objects (like byte arrays and so on).
 */
public interface IRandomAccess {
	/**
	 * Sets the offset, measured from the beginning of the data container at
	 * which the next read or write occurs. The offset may be set beyond the end
	 * of the data container. Setting the offset beyond the end of the data
	 * container does not change the data container length. The length will
	 * change only by writing after the offset has been set beyond the end of
	 * the data container.
	 * 
	 * @param offset
	 *            the offset position, measured in bytes from the beginning of
	 *            the data container
	 * @exception IOException
	 *                if <code>offset</code> is less than <code>0</code> or if
	 *                an I/O error occurs.
	 */
	public void seek(long offset) throws IOException;

	/**
	 * Sets the offset, measured from the current offset at which the next read
	 * or write occurs. The offset may be set beyond the end of the data
	 * container. Setting the offset beyond the end of the data container does
	 * not change the data container length. The length will change only by
	 * writing after the offset has been set beyond the end of the data
	 * container.
	 * 
	 * @param delta
	 *            the amount of bytes by which to change the current offset
	 *            position
	 * 
	 * @exception IOException
	 *                if the resulting <code>offset</code> is less than
	 *                <code>0</code> or if an I/O error occurs.
	 */
	public void seekBy(long delta) throws IOException;

	/**
	 * Reads a byte of data from this data container. The byte is returned as an
	 * integer in the range 0 to 255 (<code>0x00-0x0ff</code>). This method
	 * blocks if no input is yet available.
	 * <p>
	 * This method behaves in exactly the same way as the
	 * {@link InputStream#read()} method of <code>InputStream</code>.
	 * 
	 * @return the next byte of data, or <code>-1</code> if the end of the data
	 *         container has been reached.
	 * @exception IOException
	 *                if an I/O error occurs. Not thrown if the end of the data
	 *                container has been reached.
	 */
	public int read() throws IOException;

	/**
	 * Returns the current offset in this data container.
	 * 
	 * @return the offset from the beginning of the data container, in bytes, at
	 *         which the next read or write occurs.
	 * @exception IOException
	 *                if an I/O error occurs.
	 */
	public long getOffset() throws IOException;

	/**
	 * Returns the length of this data container.
	 * 
	 * @return the length of this data container, measured in bytes.
	 * @exception IOException
	 *                if an I/O error occurs.
	 */
	public long getLength() throws IOException;

	/**
	 * Assign the length. All bytes after length are truncated. If the real
	 * length is currently less than newLength, the data structure will be
	 * enlarged.
	 * 
	 * @param newLength
	 * @throws IOException
	 */
	public void setLength(long newLength) throws IOException;

	/**
	 * Reads up to <code>buffer.length</code> bytes of data from this data
	 * container into an array of bytes. This method blocks until at least one
	 * byte of input is available.
	 * <p>
	 * This method behaves in the exactly the same way as the
	 * {@link InputStream#read(byte[])} method of <code>InputStream</code>.
	 * 
	 * @param buffer
	 *            the buffer into which the data is read.
	 * @return the total number of bytes read into the buffer, or
	 *         <code>-1</code> if there is no more data because the end of this
	 *         data container has been reached.
	 * @exception IOException
	 *                if an I/O error occurs.
	 */
	public int read(byte[] buffer) throws IOException;

	/**
	 * Reads up to <code>len</code> bytes of data from this data container into
	 * an array of bytes. This method blocks until at least one byte of input is
	 * available.
	 * <p>
	 * 
	 * @param b
	 *            the buffer into which the data is read.
	 * @param off
	 *            the start offset of the data.
	 * @param len
	 *            the maximum number of bytes read.
	 * @return the total number of bytes read into the buffer, or
	 *         <code>-1</code> if there is no more data because the end of the
	 *         file has been reached.
	 * @exception IOException
	 *                if an I/O error occurs.
	 */
	public int read(byte[] buffer, int start, int numBytes) throws IOException;

	/**
	 * Closes this random access data container and releases any system
	 * resources associated with the stream. A closed random access data
	 * container cannot perform input or output operations and cannot be
	 * reopened.
	 * 
	 * @exception IOException
	 *                if an I/O error occurs.
	 */
	public void close() throws IOException;

	/**
	 * Force changes to be made persistent.
	 * 
	 * @throws IOException
	 */
	public void flush() throws IOException;

	/**
	 * <code>true</code> if this is a read only data container.
	 * 
	 * @return <code>true</code> if this is a read only data container.
	 */
	public boolean isReadOnly();

	/**
	 * Writes the specified byte . The write starts at the current offset.
	 * 
	 * @param b
	 *            the <code>byte</code> to be written.
	 * @exception IOException
	 *                if an I/O error occurs.
	 */
	public void write(int b) throws IOException;

	/**
	 * Writes <code>b.length</code> bytes from the specified byte array,
	 * starting at the current offset.
	 * 
	 * @param b
	 *            the data.
	 * @exception IOException
	 *                if an I/O error occurs.
	 */
	public void write(byte[] buffer) throws IOException;

	/**
	 * Writes <code>len</code> bytes from the specified byte array starting at
	 * <code>start</code>.
	 * 
	 * @param buffer
	 *            the data.
	 * @param start
	 *            the start offset in the data.
	 * @param numBytes
	 *            the number of bytes to write.
	 * @exception IOException
	 *                if an I/O error occurs.
	 */
	public abstract void write(byte[] buffer, int start, int numBytes)
			throws IOException;

	/**
	 * A {@link InputStream} view on the data structure.
	 * 
	 * @return A {@link InputStream} view on the data structure.
	 */
	public abstract InputStream asInputStream();

	/**
	 * A {@link OutputStream} view on the data structure.
	 * 
	 * @return A {@link OutputStream} view on the data structure.
	 */
	public abstract OutputStream asOutputStream();

	/**
	 * Mark the current offset into the data in a stack like manner.
	 */
	public abstract void mark() throws IOException;

	/**
	 * Reset to the last position on the mark-stack.
	 */
	public abstract void reset() throws IOException;
}