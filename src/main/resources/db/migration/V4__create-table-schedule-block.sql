CREATE TABLE schedule_block (
    id UUID NOT NULL,
    start_time TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    end_time TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    reason VARCHAR(255),
    professional_id UUID NOT NULL,

    CONSTRAINT pk_schedule_block PRIMARY KEY (id),
    CONSTRAINT fk_schedule_block_on_professional FOREIGN KEY (professional_id)
        REFERENCES professionals (id) ON DELETE CASCADE
);

CREATE INDEX idx_schedule_block_prof_time ON schedule_block (professional_id, start_time, end_time);