package com.almondtools.rexlex.pattern;

import static com.almondtools.rexlex.pattern.DefaultTokenType.ACCEPT;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.Matchers.contains;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.almondtools.rexlex.automaton.FromGenericAutomaton.ToCompactGenericAutomaton;

public class DefaultMatcherBuilderForAutomatonTest {

	@Test
	public void testFind() throws Exception {
		DefaultMatcherBuilder builder = DefaultMatcherBuilder.from(Pattern.compileGenericAutomaton("c"));
		Finder matcher = builder.buildFinder("abc");
		assertThat(matcher.find(), is(true));
		assertThat(matcher.match, notNullValue());
	}

	@Test
	public void testFindNot() throws Exception {
		DefaultMatcherBuilder builder = DefaultMatcherBuilder.from(Pattern.compileGenericAutomaton("c"));
		Finder matcher = builder.buildFinder("ab");
		assertThat(matcher.find(), is(false));
		assertThat(matcher.match.isMatch(), is(false));
	}

	@Test
	public void testFindDFA() throws Exception {
		DefaultMatcherBuilder builder = DefaultMatcherBuilder.from(Pattern.compileGenericAutomaton("(ab|a|bcdef|g)+"));
		Finder matcher = builder.buildFinder("xxxabcdefg");
		List<String> matches = new ArrayList<String>();
		while (matcher.find()) {
			matches.add(matcher.match.text);
		}
		assertThat(matches, hasItem("abcdefg"));
	}

	@Test
	public void testFindPosix() throws Exception {
		DefaultMatcherBuilder builder = (DefaultMatcherBuilder) new DefaultMatcherBuilder(new ToCompactGenericAutomaton()).initWith(Pattern.compileGenericAutomaton("(ab|a|bcdef|g)+"));
		Finder matcher = builder.buildFinder("xxxabcdefg");
		assertThat(matcher.find(), is(true));
		assertThat(matcher.match.text, equalTo("abcdefg"));
	}

	@Test
	public void testPattern2() throws Exception {
		DefaultMatcherBuilder builder = DefaultMatcherBuilder.from(Pattern.compileGenericAutomaton("(ab|a|bcdef|g)+"));
		Finder matcher = builder.buildFinder("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxabcdefgxxxxxxxxxabgxxxagabxxx");
		assertThat(findAll(matcher), contains(Match.create(35, "abcdefg", ACCEPT), Match.create(51, "abg", ACCEPT), Match.create(57, "agab", ACCEPT)));
	}

	public List<Match> findAll(Finder matcher) {
		List<Match> matches = new ArrayList<Match>();
		while (matcher.find()) {
			matches.add(matcher.match.copy());
		}
		return matches;
	}
}
