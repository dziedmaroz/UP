<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:my="http://fpmi.bsu.by/lectures/java" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                xsi:schemaLocation="http://fpmi.bsu.by/lectures/java library_strict.xsd"
        >
    <xsl:output method="text"/>

    <xsl:template match="/">
        <xsl:for-each select="my:library/my:book">
            # <xsl:value-of select="./@id"/>
            Book: <xsl:value-of select="my:title"/>
        </xsl:for-each>
    </xsl:template>

   <!-- <xsl:template match="/my:library">
        Before
        <xsl:apply-templates select="my:book[2]"/>
        After
    </xsl:template>

    <xsl:template match="my:book">
        # <xsl:value-of select="./@id"/>
        Book: <xsl:value-of select="my:title"/>
    </xsl:template>-->
</xsl:stylesheet>