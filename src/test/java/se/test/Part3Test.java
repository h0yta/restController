package se.test;

import static org.testng.Assert.assertEquals;

import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

public class Part3Test {
	private Part3 sut;

	@BeforeTest
	public void before() {
		sut = new Part3();
	}

	@Test
	public void test1() {
		String result = sut.run1();

		assertEquals(result, "dearalanhowareyou");
	}

	@Test
	public void test2() {
		String result = sut.run2();

		assertEquals(result, "ienjoycorresponding");
	}

	@Test
	public void test4() {
		String result = sut.run4();

		assertEquals(result, "abcd");
	}

	@Test
	public void test5() {
		String result = sut.run5();

		assertEquals(result, "IMPOSSIBLE");
	}
}
