import React, { useState } from 'react';

const Form = () => {
    const [details, setDetails] = useState({ startPoint: '', endPoint: '' });
    const [toggle, setToggle] = useState(false);

    const handleChange = (e) => {
        setDetails({ ...details, [e.target.name]: e.target.value });
    };

    const handleSubmit = (e) => {
        e.preventDefault();
        console.log('Testing Form :', details);
    };

    const handleToggle = () => {
        setToggle(!toggle);
    };

    const SR_RFAI = ["SR Pending", "SP Pending", "SO Pending", "RFAI Pending"];
    const Material = ["MO Pending", "Material Delivery Pending"];
    const INC = ["HOP Alignment Pending/Phy-AT Pending", "HOP Alignment Pending/Phy-AT Raised", "HOP Alignment Pending/Phy-AT Rejected", "HOP Alignment Pending/Phy-AT Accept", "I&C Pending"];
    const AT = ["PHY-AT RAISED/SOFT-AT PENDING", "PHY-AT REJECTED/SOFT-AT RAISED", "PHY-AT RAISED/SOFT-AT RAISED", "AT ACCEPTED", "PHY-AT PENDING/SOFT-AT ACCEPTED", "PHY-AT ACCEPTED/SOFT-AT PENDING", "PHY+SOFT AT PENDING", "PHY-AT ACCEPTED/SOFT-AT REJECTED", "PHY-AT PENDING/SOFT-AT RAISED", "PHY-AT RAISED/SOFT-AT REJECTED", "PHY-AT REJECTED/SOFT-AT REJECTED", "PHY-AT PENDING/SOFT-AT REJECTED", "PHY-AT REJECTED/SOFT-AT ACCEPTED", "PHY-AT ACCEPTED/SOFT-AT RAISED", "PHY-AT REJECTED/SOFT-AT PENDING", "PHY-AT RAISED/SOFT-AT ACCEPTED"];
    const Traffic = ["TS Completed"];
    const Cancel = ["Canceled", "Request for Cancellation", "Material Returned"];
    const softUpgrade = ["Upgrade Pending", "Upgrade Completed"];

    return (
        <div className="container">
            <button className="toggle-button" onClick={handleToggle}>
                {toggle ? 'On' : 'Off'}
            </button>
            <h1>DPR Automation Testing</h1>
            {toggle ? (
                <form onSubmit={handleSubmit}>
                    <div className="form-group">
                        <label htmlFor="startPoint">Start Point</label>
                        <select id="startPoint" name="startPoint" value={details.startPoint} onChange={handleChange}>
                            <option value="">Select Start Point</option>
                            {SR_RFAI.map((item, index) => (
                                <option key={index} value={item}>{item}</option>
                            ))}
                            {Material.map((item, index) => (
                                <option key={index} value={item}>{item}</option>
                            ))}
                            {INC.map((item, index) => (
                                <option key={index} value={item}>{item}</option>
                            ))}
                            {AT.map((item, index) => (
                                <option key={index} value={item}>{item}</option>
                            ))}
                            {Traffic.map((item, index) => (
                                <option key={index} value={item}>{item}</option>
                            ))}
                            {Cancel.map((item, index) => (
                                <option key={index} value={item}>{item}</option>
                            ))}
                            {softUpgrade.map((item, index) => (
                                <option key={index} value={item}>{item}</option>
                            ))}
                        </select>
                    </div>
                    <div className="form-group">
                        <label htmlFor="endPoint">End Point</label>
                        <select id="endPoint" name="endPoint" value={details.endPoint} onChange={handleChange}>
                            <option value="">Select End Point</option>
                            {SR_RFAI.map((item, index) => (
                                <option key={index} value={item}>{item}</option>
                            ))}
                            {Material.map((item, index) => (
                                <option key={index} value={item}>{item}</option>
                            ))}
                            {INC.map((item, index) => (
                                <option key={index} value={item}>{item}</option>
                            ))}
                            {AT.map((item, index) => (
                                <option key={index} value={item}>{item}</option>
                            ))}
                            {Traffic.map((item, index) => (
                                <option key={index} value={item}>{item}</option>
                            ))}
                            {Cancel.map((item, index) => (
                                <option key={index} value={item}>{item}</option>
                            ))}
                            {softUpgrade.map((item, index) => (
                                <option key={index} value={item}>{item}</option>
                            ))}
                        </select>
                    </div>
                    <button type="submit">Submit</button>
                </form>
            ) : (
                <div className="under-construction">
                    <p>Under Construction</p>
                </div>
            )}
        </div>
    );
};

export default Form;
