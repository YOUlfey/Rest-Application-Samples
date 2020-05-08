<?xml version="1.0" encoding="UTF-8"?>

<xsl:stylesheet version="1.0"
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
    <xsl:output method="html" indent="yes"/>
    <xsl:template match="/Collection">
        <html>
            <body>
                <table align="center">
                    <thead>
                        <th>Id</th>
                        <th>Name</th>
                        <th>description</th>
                    </thead>
                    <tbody>
                        <xsl:for-each select="item">
                            <tr>
                                <td><xsl:value-of select="id"/></td>
                                <td><xsl:value-of select="firstName"/></td>
                                <td><xsl:value-of select="lastName"/></td>
                            </tr>
                        </xsl:for-each>
                    </tbody>

                </table>
            </body>
        </html>
    </xsl:template>
</xsl:stylesheet>