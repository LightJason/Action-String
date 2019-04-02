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

// -----
// agent for testing string calls
// @iteration 2
// @testcount 5
// -----

// initial-goal
!test.

/**
 * test string
 */
+!test <-
    SBase64 = .string/base64encode( "Base64 encoded string" );
    .test/result( .test/equal( SBase64, "QmFzZTY0IGVuY29kZWQgc3RyaW5n" ), "string base64 has been failed" );

    SReverse = .string/reverse( "abcdefg" );
    .test/result( .test/equal( SReverse, "gfedcba" ), "string reverse has been failed" );

    SUpper = .string/upper("AbCdefg");
    .test/result( .test/equal( SUpper, "ABCDEFG" ), "string upper has been failed" );

    SLower = .string/lower("AbCdefg");
    .test/result( .test/equal( SLower, "abcdefg" ), "string lower has been failed" );

    SReplace = .string/replace( "1", "-", "a1b1defg1xyz1ui" );
    .test/result( .test/equal( SReplace, "a-b-defg-xyz-ui" ), "string replace has been failed" );

    SRand = .string/random( "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789", 20 );
    .test/print("string", SBase64, "--", SReverse, "--", SUpper, "--", SLower, "--", SReplace, "--", SRand );

    .test/print("string executed completly")
.