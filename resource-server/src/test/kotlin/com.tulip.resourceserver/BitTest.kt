package com.tulip.resourceserver

import org.junit.jupiter.api.Test

class BitTest {


    @Test
    fun bitTest() {
        assert(bits(1, 0))
        assert(bits(2, 1))
        assert(bits(20, 4))
        assert(bits(255, 7))
        assert(bits(256, 8))
        assert(bits(4294967295 , 31))

        assert(!bits(0, 0))
        assert(!bits(1, 1))
        assert(!bits(255, 8))
        assert(!bits(256, 7))
        assert(!bits(4294967295, 32))

    }

    private fun bits(userRights: Long, bitToShift: Int): Boolean {
        return (userRights shr bitToShift) and 1 == 1L
    }
}
