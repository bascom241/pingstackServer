$$ Audience List API with Pagination and Query. 
$$ CSV uploads End Point For Subscribers for an audience.


PHASE 4
Worker processes Campaign
↓
PHASE 5
Get recipients in batches
↓
PHASE 6
Send one batch through Brevo
↓
PHASE 7
Track provider response
↓
PHASE 8
Brevo Webhooks
↓
PHASE 9
Delivery / Open / Click / Bounce tracking
↓
PHASE 10
Retries + failure handling
↓
PHASE 11
Durable queue / production architectur

A user requests processing for a queued campaign.



Pingstack validates that the campaign belongs to that user.



The API starts a background task.



The background task loads the campaign and logs that processing has started.



The API doesn't wait for the background task to finish.
