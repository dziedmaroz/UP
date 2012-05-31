<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
    <xsl:output method="text"/>

    <xsl:template match="/">
        <xsl:for-each select="library/book">
            # <xsl:value-of select="./@id"/>
            Book: <xsl:value-of select="title"/>
        </xsl:for-each>
    </xsl:template>

   <!-- <xsl:template match="/library">
        Before
        <xsl:apply-templates select="book[2]"/>
        After
    </xsl:template>

    <xsl:template match="book">
        # <xsl:value-of select="./@id"/>
        Book: <xsl:value-of select="title"/>
    </xsl:template>-->
</xsl:stylesheet>