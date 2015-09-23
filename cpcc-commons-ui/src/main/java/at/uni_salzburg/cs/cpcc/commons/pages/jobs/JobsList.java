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

package at.uni_salzburg.cs.cpcc.commons.pages.jobs;

import java.util.List;

import javax.inject.Inject;

import org.apache.tapestry5.annotations.Property;

import at.uni_salzburg.cs.cpcc.core.entities.Job;
import at.uni_salzburg.cs.cpcc.core.services.jobs.JobRepository;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

/**
 * Job list page.
 */
public class JobsList
{
    @Inject
    private JobRepository jobRepository;

    @SuppressFBWarnings(value = "URF_UNREAD_FIELD", justification = "JobsList.tml uses this variable.")
    @Property
    private List<Job> jobList;

    @Property
    private Job currentJob;

    void onActivate()
    {
        jobList = jobRepository.findAllJobs();
    }
}
