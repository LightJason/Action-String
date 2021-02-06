/*
 * @cond LICENSE
 * ######################################################################################
 * # LGPL License                                                                       #
 * #                                                                                    #
 * # This file is part of the LightJason                                                #
 * # Copyright (c) 2015-19, LightJason (info@lightjason.org)                            #
 * # This program is free software: you can redistribute it and/or modify               #
 * # it under the terms of the GNU Lesser General Public License as                     #
 * # published by the Free Software Foundation, either version 3 of the                 #
 * # License, or (at your option) any later version.                                    #
 * #                                                                                    #
 * # This program is distributed in the hope that it will be useful,                    #
 * # but WITHOUT ANY WARRANTY; without even the implied warranty of                     #
 * # MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the                      #
 * # GNU Lesser General Public License for more details.                                #
 * #                                                                                    #
 * # You should have received a copy of the GNU Lesser General Public License           #
 * # along with this program. If not, see http://www.gnu.org/licenses/                  #
 * ######################################################################################
 * @endcond
 */

package org.lightjason.agentspeak.action.string;

import com.codepoetics.protonpack.StreamUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.lightjason.agentspeak.error.context.CExecutionIllegealArgumentException;
import org.lightjason.agentspeak.language.CRawTerm;
import org.lightjason.agentspeak.language.ITerm;
import org.lightjason.agentspeak.language.execution.IContext;
import org.lightjason.agentspeak.testing.IBaseTest;

import java.nio.charset.StandardCharsets;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
import java.util.stream.Stream;


/**
 * test for string actions
 */
public final class TestCActionString extends IBaseTest
{

    /**
     * data provider generator
     * @return data
     */
    public static Stream<Arguments> generate()
    {
        return Stream.of(
                Arguments.of( Stream.of( "fooo", "#!$foo", "1234o097", "AboCDef", "foo", "BARo" ).collect( Collectors.toList() ) )
        );
    }

    /**
     * test base64 en- and decode
     *
     * @param p_input test arguments
     */
    @ParameterizedTest
    @MethodSource( "generate" )
    public void base64( final List<String> p_input )
    {
        final List<ITerm> l_return = new ArrayList<>();
        final List<ITerm> l_result = new ArrayList<>();

        Assertions.assertTrue(
            execute(
                new CBase64Encode(),
                false,
                p_input.stream().map( CRawTerm::of ).collect( Collectors.toList() ),
                l_return
            )
        );

        Assertions.assertTrue(
            execute(
                new CBase64Decode(),
                false,
                l_return,
                l_result
            )
        );

        StreamUtils.zip(
            p_input.stream(),
            l_result.stream().map( ITerm::<String>raw ),
            AbstractMap.SimpleImmutableEntry::new
        ).forEach( i -> Assertions.assertEquals( i.getValue(), i.getKey() ) );
    }


    /**
     * test base64 decode with errors
     */
    @Test
    public void base64decodeerror()
    {
        Assertions.assertThrows( CExecutionIllegealArgumentException.class,
                                 () -> new CBase64Decode().execute(
                                    false,
                                    IContext.EMPTYPLAN,
                                    Stream.of( new String( "test encodingwith german additional character: öäß".getBytes( StandardCharsets.UTF_16 ), StandardCharsets.UTF_16 ) )
                                      .map( CRawTerm::of )
                                      .collect( Collectors.toList() ),
                                    Collections.emptyList()
                                )
        );
    }


    /**
     * test concat
     *
     * @param p_input test arguments
     */
    @ParameterizedTest
    @MethodSource( "generate" )
    public void concat( final List<String> p_input )
    {
        final List<ITerm> l_return = new ArrayList<>();

        Assertions.assertTrue(
            execute(
                new CConcat(),
                false,
                p_input.stream().map( CRawTerm::of ).collect( Collectors.toList() ),
                l_return
            )
        );

        Assertions.assertEquals(
            p_input.stream().collect( Collectors.joining() ),
            l_return.get( 0 ).<String>raw()
        );
    }


    /**
     * test concat
     *
     * @param p_input test arguments
     */
    @ParameterizedTest
    @MethodSource( "generate" )
    public void contains( final List<String> p_input )
    {
        final List<ITerm> l_return = new ArrayList<>();

        Assertions.assertTrue(
            execute(
                new CContains(),
                false,
                Stream.concat(
                    Stream.of( p_input.stream().collect( Collectors.joining() ) ),
                    p_input.stream()
                ).map( CRawTerm::of ).collect( Collectors.toList() ),
                l_return
            )
        );

        Assertions.assertTrue(
            l_return.stream()
                    .allMatch( ITerm::raw )
        );
    }


    /**
     * test lower
     *
     * @param p_input test arguments
     */
    @ParameterizedTest
    @MethodSource( "generate" )
    public void lower( final List<String> p_input )
    {
        final List<ITerm> l_return = new ArrayList<>();

        Assertions.assertTrue(
            execute(
                new CLower(),
                false,
                p_input.stream().map( CRawTerm::of ).collect( Collectors.toList() ),
                l_return
            )
        );

        StreamUtils.zip(
            p_input.stream().map( i -> i.toLowerCase( Locale.ROOT ) ),
            l_return.stream().map( ITerm::<String>raw ),
            AbstractMap.SimpleImmutableEntry::new
        ).forEach( i -> Assertions.assertEquals( i.getValue(), i.getKey() ) );
    }


    /**
     * test reverse
     *
     * @param p_input test arguments
     */
    @ParameterizedTest
    @MethodSource( "generate" )
    public void reverse( final List<String> p_input )
    {
        final List<ITerm> l_return = new ArrayList<>();

        Assertions.assertTrue(
            execute(
                new CReverse(),
                false,
                p_input.stream().map( CRawTerm::of ).collect( Collectors.toList() ),
                l_return
            )
        );

        StreamUtils.zip(
            p_input.stream().map( i -> new StringBuilder( i ).reverse().toString() ),
            l_return.stream().map( ITerm::<String>raw ),
            AbstractMap.SimpleImmutableEntry::new
        ).forEach( i -> Assertions.assertEquals( i.getValue(), i.getKey() ) );
    }


    /**
     * test size
     *
     * @param p_input test arguments
     */
    @ParameterizedTest
    @MethodSource( "generate" )
    public void size( final List<String> p_input )
    {
        final List<ITerm> l_return = new ArrayList<>();

        Assertions.assertTrue(
            execute(
                new CSize(),
                false,
                p_input.stream().map( CRawTerm::of ).collect( Collectors.toList() ),
                l_return
            )
        );

        StreamUtils.zip(
            p_input.stream().mapToLong( String::length ).boxed(),
            l_return.stream().map( ITerm::<Number>raw ).map( Number::longValue ),
            AbstractMap.SimpleImmutableEntry::new
        ).forEach( i -> Assertions.assertEquals( i.getValue(), i.getKey() ) );
    }


    /**
     * test random
     *
     * @param p_input test arguments
     */
    @ParameterizedTest
    @MethodSource( "generate" )
    public void random( final List<String> p_input )
    {
        final List<ITerm> l_return = new ArrayList<>();

        Assertions.assertTrue(
            execute(
                new CRandom(),
                false,
                Stream.concat(
                    Stream.of( p_input.stream().collect( Collectors.joining() ) ),
                    p_input.stream().mapToInt( String::length ).boxed()
                ).map( CRawTerm::of ).collect( Collectors.toList() ),
                l_return
            )
        );

        StreamUtils.zip(
            p_input.stream().mapToInt( String::length ).boxed(),
            l_return.stream().map( ITerm::<String>raw ).mapToInt( String::length ).boxed(),
            AbstractMap.SimpleImmutableEntry::new
        ).forEach( i -> Assertions.assertEquals( i.getValue(), i.getKey() ) );
    }


    /**
     * test upper
     *
     * @param p_input test arguments
     */
    @ParameterizedTest
    @MethodSource( "generate" )
    public void upper( final List<String> p_input )
    {
        final List<ITerm> l_return = new ArrayList<>();

        Assertions.assertTrue(
            execute(
                new CUpper(),
                false,
                p_input.stream().map( CRawTerm::of ).collect( Collectors.toList() ),
                l_return
            )
        );

        StreamUtils.zip(
            p_input.stream().map( i -> i.toUpperCase( Locale.ROOT ) ),
            l_return.stream().map( ITerm::<String>raw ),
            AbstractMap.SimpleImmutableEntry::new
        ).forEach( i -> Assertions.assertEquals( i.getValue(), i.getKey() ) );
    }


    /**
     * test starts-with
     */
    @Test
    public void startswith()
    {
        final List<ITerm> l_return = new ArrayList<>();

        Assertions.assertTrue(
            execute(
                new CStartsWith(),
                false,
                Stream.of( "this is an input text", "this", "th", "is" ).map( CRawTerm::of ).collect( Collectors.toList() ),
                l_return
            )
        );

        Assertions.assertEquals( 3, l_return.size() );
        Assertions.assertTrue( l_return.get( 0 ).<Boolean>raw() );
        Assertions.assertTrue( l_return.get( 1 ).<Boolean>raw() );
        Assertions.assertFalse( l_return.get( 2 ).<Boolean>raw() );
    }


    /**
     * test ends-with
     */
    @Test
    public void endswidth()
    {
        final List<ITerm> l_return = new ArrayList<>();

        Assertions.assertTrue(
            execute(
                new CEndsWith(),
                false,
                Stream.of( "this is a new input text with a cool ending", "ing", "this", "g" ).map( CRawTerm::of ).collect( Collectors.toList() ),
                l_return
            )
        );

        Assertions.assertEquals( 3, l_return.size() );
        Assertions.assertTrue( l_return.get( 0 ).<Boolean>raw() );
        Assertions.assertFalse( l_return.get( 1 ).<Boolean>raw() );
        Assertions.assertTrue( l_return.get( 2 ).<Boolean>raw() );
    }


    /**
     * tets for levenshtein distance error
     */
    @Test
    public void levenshteinerror()
    {
        Assertions.assertThrows( CExecutionIllegealArgumentException.class,
                                 () -> new CLevenshtein().execute(
                                    false,
                                    IContext.EMPTYPLAN,
                                    Collections.emptyList(),
                                    Collections.emptyList()
                                )
        );
    }


    /**
     * test levenshtein distance
     */
    @Test
    public void levenshtein()
    {
        final List<ITerm> l_return = new ArrayList<>();

        Assertions.assertTrue(
            execute(
                new CLevenshtein(),
                false,
                Stream.of( "kitten", "sitting", "singing" ).map( CRawTerm::of ).collect( Collectors.toList() ),
                l_return
            )
        );

        Assertions.assertEquals( 2, l_return.size() );
        Assertions.assertEquals( 3, l_return.get( 0 ).<Number>raw().intValue() );
        Assertions.assertEquals( 5, l_return.get( 1 ).<Number>raw().intValue() );
    }


    /**
     * test normalized compression distance
     */
    @Test
    public void ncd()
    {
        final List<ITerm> l_return = new ArrayList<>();

        Assertions.assertTrue(
            execute(
                new CNCD(),
                false,
                Stream.of( "test", "tests", "this a complete other string", "test" ).map( CRawTerm::of ).collect( Collectors.toList() ),
                l_return
            )
        );

        Assertions.assertEquals( 3, l_return.size() );
        Assertions.assertEquals( 0.04878048780487805, l_return.get( 0 ).<Number>raw().doubleValue(), 0.0001 );
        Assertions.assertEquals( 0.38333333333333336, l_return.get( 1 ).<Number>raw().doubleValue(), 0.0001 );
        Assertions.assertEquals( 0, l_return.get( 2 ).<Number>raw().doubleValue(), 0 );

        Assertions.assertTrue(
            execute(
                new CNCD(),
                false,
                Stream.of( "GZIP", "test", "tests", "this a complete other string", "test" ).map( CRawTerm::of ).collect( Collectors.toList() ),
                l_return
            )
        );

        Assertions.assertEquals( 6, l_return.size() );
        Assertions.assertEquals( 0.12, l_return.get( 3 ).<Number>raw().doubleValue(), 0 );
        Assertions.assertEquals( 0.5833333333333334, l_return.get( 4 ).<Number>raw().doubleValue(),  0.0001 );
        Assertions.assertEquals( 0, l_return.get( 5 ).<Number>raw().doubleValue(), 0 );
    }


    /**
     * test normalized compression distance error
     */
    @Test
    public void ncderror()
    {
        Assertions.assertThrows( CExecutionIllegealArgumentException.class,
                                 () -> new CNCD().execute(
                                    false,
                                    IContext.EMPTYPLAN,
                                    Collections.emptyList(),
                                    Collections.emptyList()
                                )
        );
    }

    /**
     * test replace
     */
    @Test
    public void replace()
    {
        final List<ITerm> l_return = new ArrayList<>();

        new CReplace().execute(
            false,
            IContext.EMPTYPLAN,
            Stream.of( "oo", "xx", "foobar", "root" ).map( CRawTerm::of ).collect( Collectors.toList() ),
            l_return
        );

        Assertions.assertArrayEquals(
            Stream.of( "fxxbar", "rxxt" ).toArray(),
            l_return.stream().map( ITerm::raw ).toArray()
        );
    }

    /**
     * test lambda
     */
    @Test
    public void lambda()
    {
        Assertions.assertArrayEquals(
            Stream.of( "a", "b", "c", "d", "e", "f", "g" ).toArray(),
            new CLambdaStreaming().apply( "abcdefg" ).toArray()
        );
    }
}
