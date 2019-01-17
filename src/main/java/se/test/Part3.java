package se.test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Part3 {

	public String run1() {
		final String[] a = { "are", "you", "how", "alan", "dear" };
		final String[] b = { "yo", "u", "nhoware", "arala", "de" };

		return run(a, b);
	}

	public String run2() {
		final String[] a = { "i", "ing", "resp", "ond", "oyc", "hello", "enj", "or" };
		final String[] b = { "ie", "ding", "orres", "pon", "y", "hi", "njo", "c" };

		return run(a, b);
	}

	public String run4() {
		final String[] a = { "efgh", "d", "abc" };
		final String[] b = { "efgh", "cd", "ab" };

		return run(a, b);
	}

	public String run5() {
		final String[] a = { "a", "b", "c" };
		final String[] b = { "ab", "bb", "cc" };

		return run(a, b);
	}

	private String run(String[] a, String[] b) {
		List<String> copyA = removeEquals(Arrays.asList(a), Arrays.asList(b));
		List<String> copyB = removeEquals(Arrays.asList(b), Arrays.asList(a));

		String[] newA = new String[copyA.size()];
		String[] newB = new String[copyB.size()];

		String result = traverse(copyA.toArray(newA), copyB.toArray(newB), Collections.emptyList(), "", "");
		if ("".equals(result)) {
			return "IMPOSSIBLE";
		}

		return result;
	}

	private String traverse(String[] a, String[] b, List<Integer> visited, String sa, String sb) {
		if (visited.size() > 0 && sa.equals(sb)) {
			return sa;
		}

		for (Integer j = 0; j < a.length; j++) {
			if (visited.contains(j)) {
				continue;
			}

			String res = traverse(a, b, copyAndAdd(visited, j), sa + a[j], sb + b[j]);
			if (!"".equals(res)) {
				return res;
			}
		}

		return "";
	}

	private List<Integer> copyAndAdd(List<Integer> visited, Integer j) {
		List<Integer> copy = new ArrayList<>(visited);
		copy.add(j);
		return copy;
	}

	private List<String> removeEquals(List<String> a, List<String> b) {
		List<String> result = new ArrayList<>(a);
		List<Integer> same = findEqualIndecies(a, b);
		for (Integer i : same) {
			result.remove(a.get(i));
		}

		return result;
	}

	private List<Integer> findEqualIndecies(List<String> a, List<String> b) {
		List<Integer> same = new ArrayList<>();
		for (int i = 0; i < a.size(); i++) {
			if (a.get(i).equals(b.get(i))) {
				same.add(i);
			}
		}

		return same;
	}
}
