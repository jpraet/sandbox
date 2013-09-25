<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
 xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:batch="http://kszbcss.fgov.be/batch">
 <xsl:template match="batch:detail">
  <batch:transformedDetail>
   <batch:transformedValue><xsl:value-of select="batch:value" /></batch:transformedValue>
  </batch:transformedDetail>
 </xsl:template>
</xsl:stylesheet>