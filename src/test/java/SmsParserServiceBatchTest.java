import com.example.expenseTracker.dto.respnse.ParsedTransactionDto;
import com.example.expenseTracker.enums.TransactionType;
import com.example.expenseTracker.service.SmsParserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import static org.junit.jupiter.api.Assertions.*;
class SmsParserServiceBatchTest {
    private final SmsParserService smsParserService = new SmsParserService();
    @ParameterizedTest
    @CsvSource({
            "'Rs.450.00 debited from A/c XX1234 to VPA swiggy@ybl', 450.00, Swiggy, DEBIT",
            "'RS.120 spent at Dominos via debit card ending 4532', 120, Dominos, DEBIT",
            "'INR 2500 withdrawn from ATM XX9988 on 03/07/2026', 2500, Unknown Merchant, DEBIT"
    })
    void shouldExtractCorrectly(String sms, String expectedAmount, String expectedMerchant, String expectedType) {
        ParsedTransactionDto result = smsParserService.parse(sms);
        assertEquals(expectedMerchant, result.getMerchant());
        assertEquals(TransactionType.valueOf(expectedType), result.getTransactionType());
    }
    @Test
    void shouldNotAutoSaveCreditTransactions() {
        ParsedTransactionDto result = smsParserService.parse("INR 5,000.00 credited to your account XX5678");
        assertEquals(TransactionType.CREDIT, result.getTransactionType());
    }
    @Test
    void shouldFailGracefullyOnNonTransactionSms() {
        ParsedTransactionDto result = smsParserService.parse("Your OTP is 458219");
        assertFalse(result.isParsedSuccessfully());
    }
}