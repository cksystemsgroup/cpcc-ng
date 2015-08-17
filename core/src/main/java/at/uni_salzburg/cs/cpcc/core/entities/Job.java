// This code is part of the CPCC-NG project.
//
// Copyright (c) 2015 Clemens Krainer <clemens.krainer@gmail.com>
//
// This program is free software; you can redistribute it and/or modify
// it under the terms of the GNU General Public License as published by
// the Free Software Foundation; either version 2 of the License, or
// (at your option) any later version.
//
// This program is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
// GNU General Public License for more details.
//
// You should have received a copy of the GNU General Public License
// along with this program; if not, write to the Free Software Foundation,
// Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301, USA.

package at.uni_salzburg.cs.cpcc.core.entities;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

/**
 * Job entity.
 */
@Entity
@Table(name = "jobs")
public class Job
{
    private static final int MAX_QUEUE_LENGTH = 20;
    private static final int MAX_STATUS_LENGTH = 20;
    private static final int MAX_PARAMETERS_LENGTH = 512;

    @Id
    @GeneratedValue
    private Integer id;

    @Column(name = "queue_name", nullable = false, length = MAX_QUEUE_LENGTH)
    private String queueName;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = MAX_STATUS_LENGTH)
    private JobStatus status;

    @Column(name = "parameters", nullable = false, length = MAX_PARAMETERS_LENGTH)
    private String parameters;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_time", nullable = false)
    private Date created;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "queued_time")
    private Date queued;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "start_time")
    private Date start;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "end_time")
    private Date end;

    @Lob
    @Column(name = "result_text")
    private String resultText;

    @Lob
    @Column(name = "data")
    private byte[] data;

    /**
     * @param id the id to set.
     */
    public void setId(Integer id)
    {
        this.id = id;
    }

    /**
     * @return the id
     */
    public Integer getId()
    {
        return id;
    }

    /**
     * @return the queueName
     */
    public String getQueueName()
    {
        return queueName;
    }

    /**
     * @param queueName the queueName to set
     */
    public void setQueueName(String queueName)
    {
        this.queueName = queueName;
    }

    /**
     * @return the status
     */
    public JobStatus getStatus()
    {
        return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(JobStatus status)
    {
        this.status = status;
    }

    /**
     * @return the parameters
     */
    public String getParameters()
    {
        return parameters;
    }

    /**
     * @param parameters the parameters to set
     */
    public void setParameters(String parameters)
    {
        this.parameters = parameters;
    }

    /**
     * @return the created
     */
    @SuppressFBWarnings(value = "EI_EXPOSE_REP", justification = "Exposed on purpose")
    public Date getCreated()
    {
        return created;
    }

    /**
     * @param created the created to set
     */
    @SuppressFBWarnings(value = "EI_EXPOSE_REP2", justification = "Exposed on purpose")
    public void setCreated(Date created)
    {
        this.created = created;
    }

    /**
     * @return the queued
     */
    @SuppressFBWarnings(value = "EI_EXPOSE_REP", justification = "Exposed on purpose")
    public Date getQueued()
    {
        return queued;
    }

    /**
     * @param queued the queued to set
     */
    @SuppressFBWarnings(value = "EI_EXPOSE_REP2", justification = "Exposed on purpose")
    public void setQueued(Date queued)
    {
        this.queued = queued;
    }

    /**
     * @return the start
     */
    @SuppressFBWarnings(value = "EI_EXPOSE_REP", justification = "Exposed on purpose")
    public Date getStart()
    {
        return start;
    }

    /**
     * @param start the start to set
     */
    @SuppressFBWarnings(value = "EI_EXPOSE_REP2", justification = "Exposed on purpose")
    public void setStart(Date start)
    {
        this.start = start;
    }

    /**
     * @return the end
     */
    @SuppressFBWarnings(value = "EI_EXPOSE_REP", justification = "Exposed on purpose")
    public Date getEnd()
    {
        return end;
    }

    /**
     * @param end the end to set
     */
    @SuppressFBWarnings(value = "EI_EXPOSE_REP2", justification = "Exposed on purpose")
    public void setEnd(Date end)
    {
        this.end = end;
    }

    /**
     * @return the result text, or null.
     */
    public String getResultText()
    {
        return resultText;
    }

    /**
     * @param resultText the result text to set.
     */
    public void setResultText(String resultText)
    {
        this.resultText = resultText;
    }

    /**
     * @return the job's data
     */
    @SuppressFBWarnings(value = "EI_EXPOSE_REP", justification = "Exposed on purpose")
    public byte[] getData()
    {
        return data;
    }

    /**
     * @param data the job's data to set
     */
    @SuppressFBWarnings(value = "EI_EXPOSE_REP2", justification = "Exposed on purpose")
    public void setData(byte[] data)
    {
        this.data = data;
    }

}
