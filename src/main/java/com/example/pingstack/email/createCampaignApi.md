Phase 1 ==> Create a campaign Api. 

Frontend sends this as a request to POST /api/v1/email/campaigns ==>   {
"name": "September Product Update",
"templateId": "9f866ebe-c491-4cc5-94c2-56f6cbeb1fd6",
"recipients": [
{
"email": "john@example.com",
"name": "John"
},
{
"email": "mary@example.com",
"name": "Mary"
}
]
}

Request
↓
Validate request
↓
Find template
↓
Find current template version
↓
Create EmailCampaign
↓
Create EmailRecipients
↓
Return campaign ID

We are NOT sending anything yet.

No Brevo.

No worker.

No queue.

No chunking.

No webhooks.

Just create the campaign.