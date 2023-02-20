package com.tsel.mepro.quotedetails.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.tsel.mepro.quotedetails.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CpqQuotedetailsTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CpqQuotedetails.class);
        CpqQuotedetails cpqQuotedetails1 = new CpqQuotedetails();
        cpqQuotedetails1.setId(1L);
        CpqQuotedetails cpqQuotedetails2 = new CpqQuotedetails();
        cpqQuotedetails2.setId(cpqQuotedetails1.getId());
        assertThat(cpqQuotedetails1).isEqualTo(cpqQuotedetails2);
        cpqQuotedetails2.setId(2L);
        assertThat(cpqQuotedetails1).isNotEqualTo(cpqQuotedetails2);
        cpqQuotedetails1.setId(null);
        assertThat(cpqQuotedetails1).isNotEqualTo(cpqQuotedetails2);
    }
}
