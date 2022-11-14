alter table feedback_notification
    add column first_notification_date_time timestamp;
alter table feedback_notification
    add column repeated_notification_date_time timestamp;
