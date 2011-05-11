/*
 * The MIT License
 *
 * Copyright 2011 Javier Morales <moralesjm at gmail.com>.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.withbytes.tentaculo.traverser;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Javier Morales <moralesjm at gmail.com>
 */
public class TripleTest {
    
    public TripleTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of getFirst method, of class Triple.
     */
    @Test
    public void testGetFirst() {
        Triple<Integer,String,Float> instance = new Triple<Integer, String, Float>(Integer.SIZE, "JM", Float.NaN);
        Object expResult = Integer.SIZE;
        Object result = instance.getFirst();
        assertEquals(expResult, result);
    }

    /**
     * Test of getSecond method, of class Triple.
     */
    @Test
    public void testGetSecond() {
        Triple<Integer,String,Float> instance = new Triple<Integer, String, Float>(Integer.SIZE, "JM", Float.NaN);
        Object expResult = "JM";
        Object result = instance.getSecond();
        assertEquals(expResult, result);
    }

    /**
     * Test of getThird method, of class Triple.
     */
    @Test
    public void testGetThird() {
        Triple<Integer,String,Float> instance = new Triple<Integer, String, Float>(Integer.SIZE, "JM", Float.NaN);
        Object expResult = Float.NaN;
        Object result = instance.getThird();
        assertEquals(expResult, result);
    }

    /**
     * Test of equals method, of class Triple.
     */
    @Test
    public void testEquals() {
        Triple<Integer,String,Float> instance = new Triple<Integer, String, Float>(Integer.SIZE, "JM", Float.NaN);
        Triple<Integer,String,Float> instance2 = new Triple<Integer, String, Float>(Integer.SIZE, "JM", Float.NaN);        
        Triple<Integer,String,Float> instance3 = new Triple<Integer, String, Float>(Integer.MIN_VALUE, "Javier", Float.NEGATIVE_INFINITY);
        Triple<Integer,String,Float> instance4 = new Triple<Integer, String, Float>(Integer.SIZE, "JM", Float.NEGATIVE_INFINITY);
        Triple<Integer,String,Float> instance5 = new Triple<Integer, String, Float>(Integer.SIZE, "Something", Float.NaN);
        assertEquals(true, instance.equals(instance2));
        assertEquals(false, instance.equals(instance3));
        assertEquals(false, instance.equals(instance4));
        assertEquals(false, instance.equals(instance5));
    }

}
