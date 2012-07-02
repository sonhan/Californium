/*******************************************************************************
 * Copyright (c) 2012, Institute for Pervasive Computing, ETH Zurich.
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 * 3. Neither the name of the Institute nor the names of its contributors
 *    may be used to endorse or promote products derived from this software
 *    without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE INSTITUTE AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED.  IN NO EVENT SHALL THE INSTITUTE OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS
 * OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT
 * LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY
 * OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 * 
 * This file is part of the Californium (Cf) CoAP framework.
 ******************************************************************************/
package ch.ethz.inf.vs.californium.test;

import static org.junit.Assert.assertArrayEquals;

import org.junit.Test;

import ch.ethz.inf.vs.californium.dtls.Handshaker;

public class PRFTest {
	@Test
	public void testSHA256PRF() {
		/*
		 * Found here:
		 * http://www.ietf.org/mail-archive/web/tls/current/msg03416.html
		 */
		byte[] secret = new byte[] { (byte) 0x9b, (byte) 0xbe, (byte) 0x43, (byte) 0x6b, (byte) 0xa9, (byte) 0x40, (byte) 0xf0, (byte) 0x17, (byte) 0xb1, (byte) 0x76, (byte) 0x52, (byte) 0x84, (byte) 0x9a, (byte) 0x71, (byte) 0xdb, (byte) 0x35 };
		String label = "test label";
		byte[] seed = new byte[] { (byte) 0xa0, (byte) 0xba, (byte) 0x9f, (byte) 0x93, (byte) 0x6c, (byte) 0xda, (byte) 0x31, (byte) 0x18, (byte) 0x27, (byte) 0xa6, (byte) 0xf7, (byte) 0x96, (byte) 0xff, (byte) 0xd5, (byte) 0x19, (byte) 0x8c };

		byte[] actual = Handshaker.doPRF(secret, label, seed);
		byte[] expected = new byte[] { (byte) 0xe3, (byte) 0xf2, (byte) 0x29, (byte) 0xba, (byte) 0x72, (byte) 0x7b, (byte) 0xe1, (byte) 0x7b, (byte) 0x8d, (byte) 0x12, (byte) 0x26, (byte) 0x20, (byte) 0x55, (byte) 0x7c, (byte) 0xd4, (byte) 0x53,
				(byte) 0xc2, (byte) 0xaa, (byte) 0xb2, (byte) 0x1d, (byte) 0x07, (byte) 0xc3, (byte) 0xd4, (byte) 0x95, (byte) 0x32, (byte) 0x9b, (byte) 0x52, (byte) 0xd4, (byte) 0xe6, (byte) 0x1e, (byte) 0xdb, (byte) 0x5a, (byte) 0x6b, (byte) 0x30,
				(byte) 0x17, (byte) 0x91, (byte) 0xe9, (byte) 0x0d, (byte) 0x35, (byte) 0xc9, (byte) 0xc9, (byte) 0xa4, (byte) 0x6b, (byte) 0x4e, (byte) 0x14, (byte) 0xba, (byte) 0xf9, (byte) 0xaf, (byte) 0x0f, (byte) 0xa0, (byte) 0x22, (byte) 0xf7,
				(byte) 0x07, (byte) 0x7d, (byte) 0xef, (byte) 0x17, (byte) 0xab, (byte) 0xfd, (byte) 0x37, (byte) 0x97, (byte) 0xc0, (byte) 0x56, (byte) 0x4b, (byte) 0xab, (byte) 0x4f, (byte) 0xbc, (byte) 0x91, (byte) 0x66, (byte) 0x6e, (byte) 0x9d,
				(byte) 0xef, (byte) 0x9b, (byte) 0x97, (byte) 0xfc, (byte) 0xe3, (byte) 0x4f, (byte) 0x79, (byte) 0x67, (byte) 0x89, (byte) 0xba, (byte) 0xa4, (byte) 0x80, (byte) 0x82, (byte) 0xd1, (byte) 0x22, (byte) 0xee, (byte) 0x42, (byte) 0xc5,
				(byte) 0xa7, (byte) 0x2e, (byte) 0x5a, (byte) 0x51, (byte) 0x10, (byte) 0xff, (byte) 0xf7, (byte) 0x01, (byte) 0x87, (byte) 0x34, (byte) 0x7b, (byte) 0x66 };
		
		assertArrayEquals(expected, actual);
	}
}
