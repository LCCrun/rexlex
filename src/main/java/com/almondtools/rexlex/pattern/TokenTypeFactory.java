package com.almondtools.rexlex.pattern;

import com.almondtools.rexlex.TokenType;

public interface TokenTypeFactory {

	TokenType union(TokenType type1, TokenType type2);

	TokenType union(Iterable<? extends TokenType> types);

	TokenType intersect(TokenType type1, TokenType type2);

	TokenType intersect(Iterable<? extends TokenType> types);

	TokenType errorType();

}
