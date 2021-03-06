package com.almondtools.rexlex.pattern;

import java.util.Comparator;

import com.almondtools.rexlex.TokenType;

public class DefaultTokenTypeFactory implements TokenTypeFactory {

	private Comparator<TokenType> comparator;

	public DefaultTokenTypeFactory() {
		comparator = new TokenTypeComparator();
	}
	
	@Override
	public TokenType union(TokenType type1, TokenType type2) {
		if (type1 == null) {
			return type2;
		} else if (type2 == null) {
			return type1;
		}
		if (type1 == type2) {
			return type1;
		}
		
		int compare = comparator.compare(type1, type2);
		if (compare < 0) {
			return type2;
		} else if (compare > 0) {
			return type1;
		} else if (type1 instanceof DefaultTokenType && type2 instanceof DefaultTokenType) {
			if (((DefaultTokenType) type1).compareTo((DefaultTokenType) type2) > 0) {
				return type1;
			} else {
				return type2;
			}
		} else if (type1 instanceof DefaultTokenType) {
			return type1;
		} else if (type2 instanceof DefaultTokenType) {
			return type2;
		} else {
			return null;
		}
	}

	@Override
	public TokenType union(Iterable<? extends TokenType> types) {
		TokenType type = null;
		for (TokenType current : types) {
			type = union(type, current);
		}
		return type;
	}

	@Override
	public TokenType intersect(TokenType type1, TokenType type2) {
		if (type1 == null) {
			return null;
		} else if (type2 == null) {
			return null;
		}
		if (type1 == type2) {
			return type1;
		}
		if (type1.error() || type2.error()) {
			return union(type1, type2);
		} else if (type1.accept() && type2.accept()) {
			return union(type1, type2);
		} else {
			return null;
		}
	}
	
	@Override
	public TokenType intersect(Iterable<? extends TokenType> types) {
		TokenType type = null;
		for (TokenType current : types) {
			if (type == null) {
				type = current;
			} else {
				type = intersect(type, current);
			}
		}
		return type;
	}
	
	@Override
	public TokenType errorType() {
		return DefaultTokenType.ERROR;
	}

}