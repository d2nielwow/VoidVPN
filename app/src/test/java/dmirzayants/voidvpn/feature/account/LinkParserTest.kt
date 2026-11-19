package dmirzayants.voidvpn.feature.account

import dmirzayants.voidvpn.feature.account.domain.LinkParser
import dmirzayants.voidvpn.feature.vpn.domain.VpnProtocol
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class LinkParserTest {

    @Test
    fun `vless link is parsed with protocol and host`() {
        val link = LinkParser.parse("vless://8f2c-e91a@nl-ams.void.net:443?security=reality")
        assertNotNull(link)
        assertEquals(VpnProtocol.VLESS_REALITY, link!!.protocol)
        assertEquals("nl-ams.void.net", link.host)
        assertFalse(link.isSubscription)
    }

    @Test
    fun `hy2 and hysteria2 map to Hysteria2`() {
        assertEquals(VpnProtocol.HYSTERIA2, LinkParser.parse("hy2://pass@host.net:443")!!.protocol)
        assertEquals(VpnProtocol.HYSTERIA2, LinkParser.parse("hysteria2://pass@host.net:443")!!.protocol)
    }

    @Test
    fun `trojan and shadowsocks are recognised`() {
        assertEquals(VpnProtocol.TROJAN, LinkParser.parse("trojan://pass@host.net:443")!!.protocol)
        assertEquals(VpnProtocol.SHADOWSOCKS, LinkParser.parse("ss://abc@host.net:8388")!!.protocol)
    }

    @Test
    fun `https link with sub path is a subscription`() {
        val link = LinkParser.parse("https://panel.example.com/sub/abc123")
        assertNotNull(link)
        assertTrue(link!!.isSubscription)
        assertNull(link.protocol)
        assertEquals("panel.example.com", link.host)
    }

    @Test
    fun `https link without sub path is rejected`() {
        assertNull(LinkParser.parse("https://example.com/about"))
    }

    @Test
    fun `surrounding spaces are trimmed`() {
        assertNotNull(LinkParser.parse("  vless://id@host.net:443  "))
    }

    @Test
    fun `empty, plain text and unknown schemes are rejected`() {
        assertNull(LinkParser.parse(""))
        assertNull(LinkParser.parse("hello world"))
        assertNull(LinkParser.parse("VOID7F3KQ9LM"))
        assertNull(LinkParser.parse("ftp://host.net/file"))
    }

    @Test
    fun `text with inner whitespace is rejected`() {
        assertNull(LinkParser.parse("vless://id@host.net:443 extra"))
    }
}
