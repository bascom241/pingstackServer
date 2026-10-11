[ User UI ]                   [ Your Spring Boot ]               [ Brevo API ]               [ Cloudflare DNS ]
|                                 |                              |                              |
1.  | Types "kolekto.com.ng"          |                              |                              |
2.  |-------------------------------->| POST /senders/domains        |                              |
3.  |                                 |----------------------------->|                              |
4.  | Displays TXT & CNAME records    |<-----------------------------|                              |
    |                                                                                               |
    =====|==================== 🛠️ MANUAL USER STEP IN CLOUDFLARE ========================================|=====
    |                                                                                               |
    |  A. User logs into dash.cloudflare.com                                                        |
    |  B. Selects "kolekto.com.ng" -> DNS -> Records                                                |
    |  C. Adds CNAME `brevo1._domainkey` -> `b1.kolekto-com-ng.dkim.brevo.com`                      |
    |  D. Adds CNAME `brevo2._domainkey` -> `b2.kolekto-com-ng.dkim.brevo.com`                      |
    |  E. Adds TXT `@` -> `brevo-code:fff3ebe343fd52b5222a07b7bd63ed1d`                            |
    |  F. Adds TXT `_dmarc` -> `v=DMARC1; p=none; rua=mailto:rua@dmarc.brevo.com`                    |
    |                                                                                               |
    ======================================================================================================
    |                                                                                               |
5.  | Clicks "Verify" Button          |                              |                              |
6.  |-------------------------------->| GET /domain/verify           |                              |
7.  |                                 |----------------------------->| Queries Cloudflare DNS       |
8.  |                                 |                              |----------------------------->|
9.  |                                 |                              | Returns published records    |
10. |                                 |                              |<-----------------------------|
11. |                                 | Checks if records match      |                              |
12. | UI shows "Verified" Success     |<-----------------------------|                              |